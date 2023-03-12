package controller;

import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import view.StockAppView;

/**
 * Represents an interface that helps get various inputs from user.
 */
interface AppInputCommand {

  /**
   * Gets valid date from the user.
   *
   * @param view of the application
   * @param sc   to take inputs from user
   * @return valid string of date
   */
  String handleGetDate(StockAppView view, Scanner sc);

  /**
   * Gets valid quantity of stocks from the user.
   *
   * @param ticker symbol of stock
   * @param qtyStr string representing quantity of stocks
   * @param view   of the application
   * @param sc     to take inputs from user
   * @return valid quantity of stocks
   */
  String getValidIntegerQuantity(String ticker, String qtyStr, StockAppView view, Scanner sc);

  /**
   * Gets valid name of portfolio that exists from the user.
   *
   * @param user            main model of application
   * @param view            of the application
   * @param sc              to take inputs from user
   * @param typeOfPortfolio can be flexible or inflexible portfolio
   * @return valid name of portfolio that exists
   */
  String fetchPortfolioName(User user, StockAppView view, Scanner sc,
      TypeOfPortfolio typeOfPortfolio);
}
