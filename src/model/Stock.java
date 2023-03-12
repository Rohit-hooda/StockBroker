package model;

/**
 * Represents a stock that has a name as ticker symbol in finance-world and a quantity.
 */
interface Stock {

  /**
   * Displays the stock name.
   *
   * @return String that contains a name of the stock.
   */
  String getName();

  /**
   * Displays the quantity of the stock.
   *
   * @return Double value representing the quantity of the stock.
   */
  Double getQuantity();

}
