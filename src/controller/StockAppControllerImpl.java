package controller;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;
import model.User;
import util.ApiPeriod;
import util.CsvFileUtil;
import util.FileUtil;
import util.ParamNullChecker;
import util.StockPriceUtil;
import util.StockUtil;
import util.TradeType;
import view.StockAppView;

/**
 * Represents an implementation of {@link StockAppController} that starts the stock application,
 * delegates tasks to model for handling data and view to display the result of tasks in interactive
 * manner.
 */
public final class StockAppControllerImpl implements StockAppController {

  private final String RESOURCES_DIRECTORY;
  private final String STOCK_PRICE_DATA_DIRECTORY;
  private final String API_KEY;
  private final User user;
  private final StockAppView view;
  private final InputStream inputStream;
  private final FileUtil fileUtil;
  private final StockUtil stockUtil;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of StockAppController that runs a text-based stock application and
   * co-ordinates among model and view and processes requests from text-based input stream.
   *
   * @param user                    of the stock application that can store portfolios and view
   *                                their value and composition
   * @param view                    represents a text based interface that displays appropriate
   *                                messages to keep the application interactive
   * @param inputStream             represents a stream to take inputs from the user
   * @param resourcesDirectory      represents the base location of required resources for the
   *                                application to run
   * @param stockPriceDataDirectory represents the location of file that has a table of supported
   *                                ticker names and their respective prices on a range of dates
   * @param apiKey                  to make api calls to fetch the stock data
   */
  public StockAppControllerImpl(User user, StockAppView view, InputStream inputStream,
      String resourcesDirectory, String stockPriceDataDirectory, String apiKey) {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{user, view, inputStream});
    this.user = user;
    this.view = view;
    this.inputStream = inputStream;
    this.RESOURCES_DIRECTORY = resourcesDirectory;
    this.STOCK_PRICE_DATA_DIRECTORY = resourcesDirectory + stockPriceDataDirectory;
    this.API_KEY = apiKey;
    stockPriceMap = new HashMap<>();
    fileUtil = new CsvFileUtil();
    stockUtil = new StockPriceUtil();
  }

  @Override
  public void run() {
    Scanner sc = new Scanner(inputStream);
    initialiseApp();
    String userChoice;
    while (true) {
      view.showMainMenu();
      userChoice = sc.nextLine();
      AppCommand command = null;
      if (userChoice.equals("1")) {
        command = new InflexiblePortfolioCommand(stockPriceMap, RESOURCES_DIRECTORY, API_KEY);
      } else if (userChoice.equals("2")) {
        command = new FlexiblePortfolioCommand(stockPriceMap, API_KEY);
      } else if (userChoice.equals("3")) {
        command = new CommissionChargeCommand();
      } else if (userChoice.equals("exit")) {
        break;
      } else {
        view.showInvalidUserInput();
      }
      if (command != null) {
        command.execute(user, view, sc);
      }
    }
    sc.close();
    saveFlexiblePortfolios();
    cleanupStockPriceDataDirectory();
    view.showExitAppMessage();
  }

  private void saveFlexiblePortfolios() {
    List<String> flexiblePortfolioNames = user.listAllFlexiblePortfolioNames();
    for (String flexiblePortfolioName : flexiblePortfolioNames) {
      user.savePortfolio(flexiblePortfolioName, RESOURCES_DIRECTORY + "flexible_portfolios/",
          fileUtil);
    }
  }

  private void cleanupStockPriceDataDirectory() {
    File resourceDir = new File(STOCK_PRICE_DATA_DIRECTORY);
    if (resourceDir.isDirectory()) {
      for (File file : Objects.requireNonNull(resourceDir.listFiles())) {
        deleteTempFiles(file.getPath());
      }
    }
  }

  private void deleteTempFiles(String fileName) {
    File file;
    file = new File(fileName);
    file.delete();
  }

  private void initialiseApp() {
    view.showAppStart();
    view.showInitialDataIsLoading();
    loadStockPriceDataFromCache();
    loadExistingInflexiblePortfolios();
    loadExistingFlexiblePortfolios();
    view.showInitializationComplete();
  }

  private void loadStockPriceDataFromCache() {
    Map<String, List<String[]>> cacheStockPrices = fileUtil.readAllFiles(
        RESOURCES_DIRECTORY + "stock_value_data_cache/");
    for (Map.Entry<String, List<String[]>> cache : cacheStockPrices.entrySet()) {
      List<String[]> stockDataFromFile = cache.getValue();
      Map<String, Double> stockData = stockUtil.getStockMap(stockDataFromFile, 0, 4);
      stockPriceMap.put(cache.getKey(), stockData);
    }
  }

  private void loadExistingInflexiblePortfolios() {
    Map<String, List<String[]>> existingInflexiblePortfoliosMap = user.loadExistingPortfolio(
        fileUtil, RESOURCES_DIRECTORY + "inflexible_portfolios/");

    for (Map.Entry<String, List<String[]>> portfolioData
        : existingInflexiblePortfoliosMap.entrySet()) {
      Map<String, Double> stocks = stockUtil.getStockMap(portfolioData.getValue(), 0, 1);
      try {
        stockPriceMap = stockUtil.fetchAllStockData(API_KEY, stocks.keySet(),
            stockPriceMap);
      } catch (RuntimeException re) {
        view.showLoadPortfolioByFileUnsuccessful(RESOURCES_DIRECTORY);
      }
      user.createPortfolio(stocks, portfolioData.getKey());
    }
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
}
