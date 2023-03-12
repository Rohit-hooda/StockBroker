package controller;

import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import util.TradeType;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that enable all the features for flexible
 * portfolio like creating it, showing cost basis, value and composition etc.
 */
final class FlexiblePortfolioCommand implements AppCommand {

  private final String API_KEY;
  private final Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of {@link FlexiblePortfolioCommand} that will enable all the features of
   * flexible portfolio.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param apiKey        for making the api calls to fetch data for stocks
   */
  public FlexiblePortfolioCommand(Map<String, Map<String, Double>> stockPriceMap, String apiKey) {
    this.stockPriceMap = stockPriceMap;
    this.API_KEY = apiKey;
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    String userChoice;
    while (true) {
      view.showFlexiblePortfolioMenu();
      userChoice = sc.nextLine();
      AppCommand command = null;
      if (userChoice.equals("1")) {
        createFlexiblePortfolio(user, view, sc);
      } else if (userChoice.equals("2")) {
        command = new TradeCommand(stockPriceMap, API_KEY, TradeType.BUY);
      } else if (userChoice.equals("3")) {
        command = new TradeCommand(stockPriceMap, API_KEY, TradeType.SELL);
      } else if (userChoice.equals("4")) {
        command = new CostBasisCommand(stockPriceMap);
      } else if (userChoice.equals("5")) {
        command = new StockValueCommand(stockPriceMap, TypeOfPortfolio.FLEXIBLE);
      } else if (userChoice.equals("6")) {
        command = new PortfolioPerformanceCommand(stockPriceMap);
      } else if (userChoice.equals("7")) {
        command = new StockCompositionCommand(TypeOfPortfolio.FLEXIBLE);
      } else if (userChoice.equals("8")) {
        command = new BuyFractionalShareCommand(stockPriceMap, API_KEY);
      } else if (userChoice.equals("9")) {
        command = new StrategyCommand(stockPriceMap, API_KEY);
      } else if (userChoice.equals("back")) {
        break;
      } else {
        view.showInvalidUserInput();
      }
      if (command != null) {
        command.execute(user, view, sc);
      }
    }
  }

  private void createFlexiblePortfolio(User user, StockAppView view, Scanner sc) {
    view.askNameOfPortfolio();
    String portfolioName = sc.nextLine();
    while (user.flexiblePortfolioExists(portfolioName) || user.inflexiblePortfolioExists(
        portfolioName) || portfolioName.isBlank()) {
      if (portfolioName.isBlank()) {
        view.showPortfolioNameCannotBeEmpty();
      } else {
        view.showPortfolioAlreadyExists(portfolioName);
      }
      view.askNameOfPortfolio();
      portfolioName = sc.nextLine();
    }
    user.createPortfolio(null, portfolioName);
  }

}
