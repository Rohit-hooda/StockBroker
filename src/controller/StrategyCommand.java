package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import util.ApiPeriod;
import util.StockPriceUtil;
import util.ValidDateChecker;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that enables a user to buy a set of stocks in
 * existing flexible portfolio by specifying the proportions of stocks periodically between a range
 * of dates.
 */
public class StrategyCommand implements AppCommand {

  private final AppInputCommand appInputCommand;
  private final String API_KEY;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of StrategyCommand by taking a map of stock prices on a range of dates
   * and api key to make the api call.
   *
   * @param stockPriceMap a map of stock prices on a range of dates
   * @param apiKey        api key for stocks data api
   */
  public StrategyCommand(Map<String, Map<String, Double>> stockPriceMap, String apiKey) {
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

    view.askFromDateForStrategy();
    String from = appInputCommand.handleGetDate(view, sc);

    view.askToDateForStrategy();
    String to = handleGetDateOrEmpty(view, sc);

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
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showUnsupportedTickerName(ticker);
        continue;
      }
      view.askProportionStock(ticker);
      String proportionStr = sc.nextLine();
      while (!isValidDouble(proportionStr)) {
        view.showInvalidUserInput();
        view.askProportionStock(ticker);
        proportionStr = sc.nextLine();
      }
      Double proportion = Double.parseDouble(proportionStr);
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
      view.showInvalidUserInput();
      view.askAmountToBeInvested();
      amountStr = sc.nextLine();
    }
    Double amount = Double.parseDouble(amountStr);

    view.askPeriodForStrategy();
    String period = sc.nextLine();
    while (!ApiPeriod.isAnApiPeriod(period)) {
      view.askPeriodForStrategy();
      period = sc.nextLine();
    }
    try {
      user.addStrategyToFlexiblePortfolio(portfolioName, stockPriceMap, amount, tickerRatios,
          from, to, ApiPeriod.valueOfApiPeriod(period));
      view.strategyCreated();
    } catch (IllegalArgumentException e) {
      view.strategyFailed();
    }

  }

  private String handleGetDateOrEmpty(StockAppView view, Scanner sc) {
    view.askDate();
    String dateString = sc.nextLine();
    while (!new ValidDateChecker().apply(dateString)) {
      if (dateString.isEmpty()) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.now();
        yesterday = yesterday.minusDays(1);
        dateString = formatter.format(yesterday);
        break;
      }
      view.showInvalidUserInput();
      view.askDate();
      dateString = sc.nextLine();
    }
    return dateString;
  }

  private boolean isValidDouble(String value) {
    try {
      Double nc = Double.parseDouble(value);
      return value.charAt(0) != '-' && nc > 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
