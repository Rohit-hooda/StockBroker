package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import util.ApiPeriod;
import util.StockPriceUtil;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that enables a user to buy a set of stocks in
 * existing flexible portfolio by specifying the proportions of stocks.
 */
public class BuyFractionalShareCommand implements AppCommand {

  private final AppInputCommand appInputCommand;
  private final String API_KEY;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of BuyFractionalShareCommand by taking a map of stock prices on a range
   * of dates and api key to make the api call.
   *
   * @param stockPriceMap a map of stock prices on a range of dates
   * @param apiKey        api key for stocks data api
   */
  public BuyFractionalShareCommand(Map<String, Map<String, Double>> stockPriceMap, String apiKey) {
    this.appInputCommand = new StockAppInputCommand();
    this.stockPriceMap = stockPriceMap;
    this.API_KEY = apiKey;
  }


  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    Map<String, Double> tickerRatios = new HashMap<>();
    Double proportionUntilNow = 0.0;

    view.askNameOfPortfolio();
    String portfolioName = appInputCommand.fetchPortfolioName(user, view, sc,
        TypeOfPortfolio.FLEXIBLE);

    String date = appInputCommand.handleGetDate(view, sc);

    while (true) {
      view.askTickerOfStock();
      String ticker = sc.nextLine();
      if (ticker.isBlank() || ticker.isEmpty()) {
        view.showInvalidUserInput();
        continue;
      }
      try {
        String stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = new StockPriceUtil().fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
        if (!stockPriceMap.get(stockPriceDataFileName).containsKey(date)) {
          view.showStockDetailsNotAvailableOnDate(ticker, date);
          return;
        }
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showUnsupportedTickerName(ticker);
        continue;
      }
      view.askProportionStock(ticker);
      String proportionStr = sc.nextLine();
      while (!isValidDouble(proportionStr)) {
        view.askProportionStock(ticker);
        proportionStr = sc.nextLine();
      }
      double proportion = Double.parseDouble(proportionStr);
      proportionUntilNow += proportion;

      if (proportionUntilNow < 100.0001) {
        tickerRatios.put(ticker, proportion);
        if (proportionUntilNow.equals(100.0)) {
          break;
        }
      } else {
        view.showInvalidUserInput();
        return;
      }
    }
    view.askAmountToBeInvested();
    String amountStr = sc.nextLine();
    while (!isValidDouble(amountStr)) {
      view.askAmountToBeInvested();
      amountStr = sc.nextLine();
    }
    Double amount = Double.parseDouble(amountStr);

    user.addFractionalTradeToFlexiblePortfolio(portfolioName, stockPriceMap, amount, tickerRatios,
        date);
  }

  private boolean isValidDouble(String newCommission) {
    try {
      Double nc = Double.parseDouble(newCommission);
      return newCommission.charAt(0) != '-' && nc > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
