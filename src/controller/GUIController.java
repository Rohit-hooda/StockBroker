package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;
import util.ApiPeriod;
import util.CsvFileUtil;
import util.FileUtil;
import util.StockPriceUtil;
import util.StockUtil;
import util.TradeType;
import view.IView;

/**
 * Represents a controller in MVC that is responsible to control a GUI and the model to enable the
 * features of the application.
 */
public class GUIController implements Feature {

  private final FileUtil fileUtil;
  private final StockUtil stockUtil;
  private final String RESOURCES_DIRECTORY;
  private final String API_KEY;
  private final User user;
  private IView view;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of GUIController by taking the main model user, api key and path for
   * resources directory.
   *
   * @param user               main model of the application
   * @param resourcesDirectory directory of various resources
   * @param apiKey             for making API calls to fetch stock data
   */
  public GUIController(User user, String resourcesDirectory, String apiKey) {
    this.user = user;
    this.RESOURCES_DIRECTORY = resourcesDirectory;
    fileUtil = new CsvFileUtil();
    stockUtil = new StockPriceUtil();
    this.API_KEY = apiKey;
    stockPriceMap = new HashMap<>();
    loadExistingFlexiblePortfolios();
  }

  @Override
  public void setView(IView v) {
    view = v;
    view.addFeatures(this);
    List<String> portfolioNames = user.listAllFlexiblePortfolioNames();
    view.populatePortfolioMenu(portfolioNames);
  }

  private void loadExistingFlexiblePortfolios() {
    Map<String, List<String[]>> existingFlexiblePortfoliosMap = user.loadExistingPortfolio(
        fileUtil, RESOURCES_DIRECTORY + "flexible_portfolios/");
    for (Map.Entry<String, List<String[]>> portfolioData
        : existingFlexiblePortfoliosMap.entrySet()) {
      user.createPortfolio(null, portfolioData.getKey());
      for (String[] trade : portfolioData.getValue()) {
        String ticker = trade[0];
        String stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = stockUtil.fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
        String typeOfTrade = trade[3];
        double qty;
        if (typeOfTrade.equals(TradeType.SELL.getTradeType())) {
          qty = (-1.0) * Double.parseDouble(trade[1]);
        } else {
          qty = Double.parseDouble(trade[1]);
        }
        user.addTradeToFlexiblePortfolio(portfolioData.getKey(), ticker, qty, trade[2]);
      }
    }
  }

  @Override
  public void handleCreateNewPortfolio() {
    String newPortfolioName = view.getNewPortfolioName();
    if (!newPortfolioName.isEmpty() || !newPortfolioName.isBlank()) {
      user.createPortfolio(null, newPortfolioName);
      view.addNewPortfolioToMenu(newPortfolioName);
    }
  }

  @Override
  public void handleWorkWithPortfolio() {
    view.showPortfolioOptions();
  }

  @Override
  public void handleGoBack() {
    view.goBackToMainMenu();
  }

  @Override
  public void handlePortfolioValue() {
    view.showInputFormForPortfolioValue();
  }

  @Override
  public void showPortfolioValue() {
    String date = view.getValueInputs();
    String portfolioName = view.getCurrentPortfolioName();
    if (!date.isEmpty()) {
      try {
        Double value = user.getValuesOfStocksOfPortfolio(portfolioName, stockPriceMap, date);
        view.showValueOfPortfolio(value);
      } catch (IllegalArgumentException e) {
        view.showValueStatus("Value could not be found!");
      }
    }
  }

  @Override
  public void handleCompositionOfPortfolio() {
    view.showCompositionInputForm();
  }

  @Override
  public void showPortfolioComposition() {
    String date = view.getInputForComposition();
    String portfolioName = view.getCurrentPortfolioName();
    if (!date.isEmpty()) {
      view.showCompositionOfPortfolio(user.getCompositionOfPortfolio(portfolioName, date));
    }
  }

  @Override
  public void handleBuyByQuantity() {
    view.showInputFormForBuyByQty();
  }

  @Override
  public void performBuyByQty() {
    String[] buyByQuantityInput = view.getInputForBuyByQuantity();

    if (buyByQuantityInput != null) {
      String portfolioName = view.getCurrentPortfolioName();
      String ticker = buyByQuantityInput[0];
      String stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
      try {
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showBuyStatus("Unsuccessful");
        return;
      }

      Double quantity = Double.parseDouble(buyByQuantityInput[1]);
      String date = buyByQuantityInput[2];
      try {
        if (stockPriceMap.get(stockPriceDataFileName).containsKey(date)) {
          user.addTradeToFlexiblePortfolio(portfolioName, ticker, quantity, date);
          user.savePortfolio(view.getCurrentPortfolioName(),
              RESOURCES_DIRECTORY + "flexible_portfolios/", fileUtil);
          view.showBuyStatus("Successful");
        } else {
          view.showBuyStatus("Unsuccessful");
        }
      } catch (IllegalArgumentException ie) {
        view.showBuyStatus("Unsuccessful");
      }
    }
  }

  @Override
  public void handleChangeCommissionFee() {
    Double newCommission = view.getCommissionCharge(user.getCommissionCharge());
    if (newCommission > 0) {
      user.setCommissionCharge(newCommission);
    }
  }

  @Override
  public void handleSell() {
    view.showInputFormForSell();
  }

  @Override
  public void performSell() {
    String[] sellInput = view.getInputForSell();

    if (sellInput != null) {
      String portfolioName = view.getCurrentPortfolioName();
      String ticker = sellInput[0];
      String stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();

      try {
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showSellStatus("Unsuccessful");
        return;
      }

      Double quantity = (-1) * Double.parseDouble(sellInput[1]);
      String date = sellInput[2];
      try {
        if (stockPriceMap.get(stockPriceDataFileName).containsKey(date)) {
          user.addTradeToFlexiblePortfolio(portfolioName, ticker, quantity, date);
          user.savePortfolio(view.getCurrentPortfolioName(),
              RESOURCES_DIRECTORY + "flexible_portfolios/", fileUtil);
          view.showSellStatus("Successful");
        } else {
          view.showSellStatus("Unsuccessful");
        }
      } catch (IllegalArgumentException ie) {
        view.showSellStatus("Unsuccessful");
      }
    }
  }

  @Override
  public void handleCostBasis() {
    view.showInputFormForPortfolioCostBasis();
  }

  @Override
  public void showCostBasis() {
    String date = view.getCostBasisInputs();
    String portfolioName = view.getCurrentPortfolioName();
    if (!date.isEmpty()) {
      try {
        Double value = user.getCostBasisForFlexiblePortfolio(portfolioName, date, stockPriceMap);
        view.showCostBasisOfPortfolio(value);
      } catch (IllegalArgumentException e) {
        view.showCostBasisStatus("Cost Basis could not be found!");
      }
    }
  }

  @Override
  public void handleBuyByAmount() {
    view.showBuyByAmount();
  }

  @Override
  public void handleAddStocksToBuy() {
    String date = view.getDateToBuyByAmount();
    Double amount = view.getAmountToBuyByAmount();
    if (!date.isEmpty() && amount != null) {
      Integer numberOfStocks = view.getNumberOfStocksToBuy();
      view.showInputForStocksWithProportion(numberOfStocks);
    }
  }

  @Override
  public void performBuyByAmount() {
    String date = view.getDateToBuyByAmount();
    Double amount = view.getAmountToBuyByAmount();
    if (!date.isEmpty() && amount != null) {
      Map<String, Double> tickerProportions = view.getTickerProportions();
      if (tickerProportions != null) {
        for (Map.Entry<String, Double> tickerProportion : tickerProportions.entrySet()) {
          String stockPriceDataFileName =
              tickerProportion.getKey() + "_" + ApiPeriod.DAILY.getApiPeriod();

          try {
            if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
              stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY,
                  tickerProportion.getKey(),
                  stockPriceMap);
            }
          } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            view.showBuyByAmountFailed();
            return;
          }
        }
        try {
          user.addFractionalTradeToFlexiblePortfolio(view.getCurrentPortfolioName(), stockPriceMap,
              amount, tickerProportions, date);
          user.savePortfolio(view.getCurrentPortfolioName(),
              RESOURCES_DIRECTORY + "flexible_portfolios/", fileUtil);
          view.showBuyByAmountSucceeded();
        } catch (Exception e) {
          view.showBuyByAmountFailed();
        }
      }
    }
  }

  @Override
  public void handleStrategy() {
    view.showStrategyInputs();
  }

  @Override
  public void handleAddStocksInStrategy() {
    String toDateStr = view.getToDateForStrategy();
    String fromDateStr = view.getFromDateForStrategy();
    Double amount = view.getAmountForStrategy();

    if (!fromDateStr.isEmpty() && !toDateStr.isEmpty() && amount != null) {
      if (fromDateStr.compareTo(toDateStr) > 0) {
        view.showInvalidFromToDates();
        return;
      }
      Integer numberOfStocks = view.getNumberOfStocksForStrategy();
      view.showInputForStocksWithProportionForStrategy(numberOfStocks);
    }
  }

  @Override
  public void implementStrategy() {
    String toDate = view.getToDateForStrategy();
    String fromDate = view.getFromDateForStrategy();
    Double amount = view.getAmountForStrategy();
    String period = view.getPeriodForStrategy();
    if (!toDate.isEmpty() && !fromDate.isEmpty() && amount != null) {
      Map<String, Double> tickerProportions = view.getTickerProportionsForStrategy();
      if (tickerProportions != null) {
        for (Map.Entry<String, Double> tickerProportion : tickerProportions.entrySet()) {
          String stockPriceDataFileName =
              tickerProportion.getKey() + "_" + ApiPeriod.DAILY.getApiPeriod();
          try {
            if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
              stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY,
                  tickerProportion.getKey(),
                  stockPriceMap);
            }
          } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            view.showStrategyFailed();
            return;
          }
        }
        try {
          user.addStrategyToFlexiblePortfolio(view.getCurrentPortfolioName(), stockPriceMap, amount,
              tickerProportions, fromDate, toDate, ApiPeriod.valueOf(period));
          user.savePortfolio(view.getCurrentPortfolioName(),
              RESOURCES_DIRECTORY + "flexible_portfolios/", fileUtil);
          view.showStrategySucceeded();
        } catch (Exception e) {
          view.showStrategyFailed();
        }
      }
    }
  }

  @Override
  public void handlePerformance() {
    view.showPerformanceInputView();
  }

  @Override
  public void showPerformance() {
    String from = view.getFromDateForPerformance();
    String to = view.getToDateForPerformance();
    if (!from.isEmpty() && !to.isEmpty()) {
      if (from.compareTo(to) >= 0) {
        view.showInvalidFromToDates();
      } else {
        try {
          user.getPerformanceOfFlexiblePortfolio(view.getCurrentPortfolioName(), from, to,
              stockPriceMap,
              (stringDoubleMap, apiPeriod) -> view.showPerformanceView(stringDoubleMap, apiPeriod));
        } catch (IllegalArgumentException ie) {
          //do nothing
        }
      }
    }
  }
}
