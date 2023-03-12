package controller;

import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that handles the viewing of performance of
 * flexible portfolio.
 */
final class PortfolioPerformanceCommand implements AppCommand {

  private final Map<String, Map<String, Double>> stockPriceMap;
  private final AppInputCommand appInputCommand;

  /**
   * Constructs an instance of PortfolioPerformanceCommand that is responsible to show performance
   * of a flexible portfolio. data.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   */
  public PortfolioPerformanceCommand(Map<String, Map<String, Double>> stockPriceMap) {
    this.stockPriceMap = stockPriceMap;
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    String portfolioName = appInputCommand.fetchPortfolioName(user, view, sc,
        TypeOfPortfolio.FLEXIBLE);
    view.askFromDate();
    String from = appInputCommand.handleGetDate(view, sc);
    view.askToDate();
    String to = appInputCommand.handleGetDate(view, sc);
    view.showPerformanceHeader(portfolioName, from, to);
    try {
      user.getPerformanceOfFlexiblePortfolio(portfolioName, from, to, stockPriceMap,
          new PerformanceScalerCommand(view));
    } catch (IllegalArgumentException e) {
      view.showPortfolioDetailsNotAvailableForDate(portfolioName, from);
    }
  }

}
