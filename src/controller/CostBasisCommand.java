package controller;

import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import view.StockAppView;

/**
 * Accomplishes feature of showing the cost basis of a flexible portfolio in the application.
 */
final class CostBasisCommand implements AppCommand {

  private final Map<String, Map<String, Double>> stockPriceMap;
  private final AppInputCommand appInputCommand;

  /**
   * Constructs an instance of {@link CostBasisCommand} that can show cost basis of a flexible
   * portfolio.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   */
  public CostBasisCommand(Map<String, Map<String, Double>> stockPriceMap) {
    this.stockPriceMap = stockPriceMap;
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    String portfolioName = appInputCommand.fetchPortfolioName(user, view, sc,
        TypeOfPortfolio.FLEXIBLE);
    String date = appInputCommand.handleGetDate(view, sc);
    Double costBasis;
    costBasis = user.getCostBasisForFlexiblePortfolio(portfolioName, date, stockPriceMap);
    view.showCostBasisOfPortfolio(portfolioName, costBasis, date);
  }
}
