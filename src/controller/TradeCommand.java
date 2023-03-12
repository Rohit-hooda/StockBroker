package controller;

import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import util.ApiPeriod;
import util.StockPriceUtil;
import util.TradeType;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that enables a user to add a trade ie, buy or
 * sell of a stock in existing flexible portfolio.
 */
final class TradeCommand implements AppCommand {

  private final String API_KEY;
  private final TradeType tradeType;
  private final AppInputCommand appInputCommand;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of TradeCommand that performs buy or sell on a flexible portfolio.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param apiKey        to make api calls to fetch the stock data from api
   * @param tradeType     buy or sell
   */
  public TradeCommand(Map<String, Map<String, Double>> stockPriceMap, String apiKey,
      TradeType tradeType) {
    this.stockPriceMap = stockPriceMap;
    this.API_KEY = apiKey;
    this.tradeType = tradeType;
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    String portfolioName = appInputCommand.fetchPortfolioName(user, view, sc,
        TypeOfPortfolio.FLEXIBLE);
    String ticker;
    double quantity;
    String date;
    String stockPriceDataFileName;
    while (true) {
      view.askTickerOfStock();
      ticker = sc.nextLine();
      if (ticker.isBlank() || ticker.isEmpty()) {
        view.showInvalidUserInput();
        continue;
      }
      try {
        stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showUnsupportedTickerName(ticker);
        continue;
      }
      view.askQuantityOfStock(ticker);
      String qtyStr = sc.nextLine();
      qtyStr = appInputCommand.getValidIntegerQuantity(ticker, qtyStr, view, sc);
      quantity = Double.parseDouble(qtyStr);
      if (tradeType.equals(TradeType.SELL)) {
        quantity = (-1.0) * quantity;
      }
      date = handleValidDate(view, sc, ticker, stockPriceDataFileName);
      break;
    }
    try {
      if (stockPriceMap.get(stockPriceDataFileName).containsKey(date)) {
        user.addTradeToFlexiblePortfolio(portfolioName, ticker, quantity, date);
        view.showTradeSuccessful(tradeType.getTradeType());
      } else {
        view.showError("Stock Exchange not open on given date!");
      }
    } catch (IllegalArgumentException ie) {
      view.showError(ie.getMessage());
    }
  }

  private String handleValidDate(StockAppView view, Scanner sc, String ticker,
      String stockPriceDataFileName) {
    view.askDate();
    String dateString = sc.nextLine();
    while (!stockPriceMap.get(stockPriceDataFileName).containsKey(dateString)) {
      view.showStockDetailsNotAvailableOnDate(ticker, dateString);
      view.askDate();
      dateString = sc.nextLine();
    }
    return dateString;
  }
}
