package controller;

import java.util.List;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import util.ValidDateChecker;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppInputCommand} that fetches multiple kinds of inputs
 * interacting with the user using view.
 */
final class StockAppInputCommand implements AppInputCommand {

  private static String getPortfolioName(StockAppView view, Scanner sc, String portfolioName) {
    view.showPortfolioDoesNotExist(portfolioName);
    view.askNameOfPortfolio();
    portfolioName = sc.nextLine();
    return portfolioName;
  }

  @Override
  public String handleGetDate(StockAppView view, Scanner sc) {
    view.askDate();
    String dateString = sc.nextLine();
    while (!new ValidDateChecker().apply(dateString)) {
      view.showInvalidUserInput();
      view.askDate();
      dateString = sc.nextLine();
    }
    return dateString;
  }

  @Override
  public String getValidIntegerQuantity(String ticker, String qtyStr, StockAppView view,
      Scanner sc) {
    while (!inputValidIntegerQuantity(qtyStr, view)) {
      view.askQuantityOfStock(ticker);
      qtyStr = sc.nextLine();
    }
    return qtyStr;
  }

  private boolean inputValidIntegerQuantity(String qtyStr, StockAppView view) {
    try {
      Integer.parseInt(qtyStr);
      if (qtyStr.charAt(0) == '-') {
        throw new IllegalArgumentException("Quantity cannot be negative");
      }
      return true;
    } catch (IllegalArgumentException nfe) {
      view.showInvalidQuantityEntered();
    }
    return false;
  }

  @Override
  public String fetchPortfolioName(User user, StockAppView view, Scanner sc,
      TypeOfPortfolio typeOfPortfolio) {
    List<String> portfolioNames;
    if (typeOfPortfolio.equals(TypeOfPortfolio.FLEXIBLE)) {
      portfolioNames = user.listAllFlexiblePortfolioNames();
    } else {
      portfolioNames = user.listAllInflexiblePortfolioNames();
    }
    view.showAllPortfolioNames(portfolioNames);
    view.askNameOfPortfolio();
    String portfolioName = sc.nextLine();
    if (typeOfPortfolio.equals(TypeOfPortfolio.FLEXIBLE)) {
      while (!user.flexiblePortfolioExists(portfolioName)) {
        portfolioName = getPortfolioName(view, sc, portfolioName);
      }
    } else {
      while (!user.inflexiblePortfolioExists(portfolioName)) {
        portfolioName = getPortfolioName(view, sc, portfolioName);
      }
    }

    return portfolioName;
  }
}
