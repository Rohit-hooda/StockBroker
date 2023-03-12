package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import model.TypeOfPortfolio;
import model.User;
import util.ParamNullChecker;
import util.StringAppender;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that handles the viewing of composition of a
 * portfolio.
 */
final class StockCompositionCommand implements AppCommand {

  private final BiFunction<String[], String, String> stringAppender;
  private final Consumer<Object[]> nullChecker;
  private final AppInputCommand appInputCommand;
  private final TypeOfPortfolio typeOfPortfolio;

  /**
   * Constructs an instance of StockCompositionCommand that is responsible to co-ordinate with view
   * and model to enable viewing of composition of a portfolio.
   *
   * @param typeOfPortfolio can be flexible or inflexible type of portfolio
   */
  public StockCompositionCommand(TypeOfPortfolio typeOfPortfolio) {
    this.typeOfPortfolio = typeOfPortfolio;
    this.nullChecker = new ParamNullChecker();
    this.stringAppender = new StringAppender();
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    nullChecker.accept(new Object[]{user, view});
    Map<String, Double> stocks;
    String portfolioName;
    if (this.typeOfPortfolio.equals(TypeOfPortfolio.INFLEXIBLE)) {
      portfolioName = appInputCommand
          .fetchPortfolioName(user, view, sc, TypeOfPortfolio.INFLEXIBLE);
      stocks = user.getCompositionOfPortfolio(portfolioName, null);
    } else {
      portfolioName = appInputCommand
          .fetchPortfolioName(user, view, sc, TypeOfPortfolio.FLEXIBLE);
      view.askDateForComposition(portfolioName);
      String date = appInputCommand.handleGetDate(view, sc);
      stocks = user.getCompositionOfPortfolio(portfolioName, date);
    }
    List<String> stocksAsString = new ArrayList<>();
    String[] stockDataArray = {"Ticker", "Quantity"};
    String header = stringAppender.apply(stockDataArray, " : ");
    for (Map.Entry<String, Double> stockData : stocks.entrySet()) {
      stocksAsString.add(stringAppender.apply(
          new String[]{stockData.getKey(), String.valueOf(stockData.getValue())}, " : "));
    }
    stocksAsString.add(0, header);
    view.showPortfolioDetails(stocksAsString, portfolioName, 0.0, null);
  }
}
