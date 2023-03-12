package controller;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import model.TypeOfPortfolio;
import model.User;
import util.ParamNullChecker;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that handles the viewing of value of a
 * portfolio.
 */
final class StockValueCommand implements AppCommand {

  private final Map<String, Map<String, Double>> stockPriceMap;
  private final Consumer<Object[]> nullChecker;
  private final TypeOfPortfolio typeOfPortfolio;
  private final AppInputCommand appInputCommand;


  /**
   * Constructs an instance of StockValueCommand that is responsible to co-ordinate with view and
   * model to enable viewing of composition of a portfolio.
   *
   * @param stockPriceMap represents a map of maps that represents stocks and their prices on a
   *                      range of dates
   */
  public StockValueCommand(Map<String, Map<String, Double>> stockPriceMap,
      TypeOfPortfolio typeOfPortfolio) {
    nullChecker = new ParamNullChecker();
    this.stockPriceMap = stockPriceMap;
    this.typeOfPortfolio = typeOfPortfolio;
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    nullChecker.accept(new Object[]{user, view, sc});
    String portfolioName = appInputCommand.fetchPortfolioName(user, view, sc, typeOfPortfolio);
    String date = appInputCommand.handleGetDate(view, sc);
    try {
      Double portfolioValue = user.getValuesOfStocksOfPortfolio(portfolioName,
          stockPriceMap, date);
      view.showPortfolioValue(portfolioName, date, portfolioValue);
    } catch (IllegalArgumentException ie) {
      view.showPortfolioDetailsNotAvailableForDate(portfolioName, date);
    }
  }

}
