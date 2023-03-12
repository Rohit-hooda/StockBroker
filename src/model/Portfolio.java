package model;

import java.util.Map;
import util.FileUtil;

/**
 * Represents a Portfolio that can have a collection of multiple stock details in it.
 */
interface Portfolio {

  /**
   * Displays the name of the portfolio.
   *
   * @return String that contains the name of the portfolio.
   */
  String getPortfolioName();

  /**
   * Displays a list of stocks that are in this portfolio on a particular date.
   *
   * @return List of Stock names with their quantities on a specific date
   */
  Map<String, Double> getComposition(String date);


  /**
   * Fetches the value of a portfolio on a given date.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param date          on which value of portfolio is asked
   * @return value of portfolio on given date
   * @throws IllegalArgumentException if there is no data present for stock and its prices in last
   *                                  100 days from date asked
   * @throws NullPointerException     if date or stockPriceMap are null
   */
  Double getValue(Map<String, Map<String, Double>> stockPriceMap, String date)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Saves a portfolio onto a file.
   *
   * @param filePath path of file where portfolio needs to be saved
   * @param fileUtil helps take data of portfolio and performs file IO operation to write the file
   */
  void savePortfolio(String filePath, FileUtil fileUtil);

}
