package model;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import util.ApiPeriod;
import util.FileUtil;

/**
 * Represents a user of the stock application that can create and store portfolios of stocks,
 * perform buy or sell of stocks and view their composition, values cost basis.
 */
public interface User {

  /**
   * Creates a unique flexible ir inflexible portfolio for the user taking in data for list of
   * stocks and a name for the portfolio.
   *
   * @param stocks        represents a table stock name and their respective quantities
   * @param portfolioName name of the portfolio to be created
   * @throws IllegalArgumentException if exists, blank portfolioName
   */
  void createPortfolio(Map<String, Double> stocks, String portfolioName)
      throws IllegalArgumentException;

  /**
   * Adds a trade to an existing flexible portfolio.
   *
   * @param portfolioName name of flexible portfolio
   * @param ticker        name of stock
   * @param quantity      quantity of stocks trading in
   * @param date          of trade
   */
  void addTradeToFlexiblePortfolio(String portfolioName, String ticker, Double quantity,
      String date);

  /**
   * Adds trades that can be divided according to the ratios provided and within an amount into the
   * given portfolio.
   *
   * @param portfolioName name of the flexible portfolio
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param amount        of money to be traded
   * @param tickerRatios  a map of proportion of stocks in which maount will be divided
   * @param date          on which to perform this trade of multiple stocks
   */
  void addFractionalTradeToFlexiblePortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String date);

  /**
   * Fetches the current value of commission charge in the app.
   *
   * @return value of commission charge
   */
  Double getCommissionCharge();

  /**
   * Sets a commission charge per that is added per buy or sell a user performs on the application.
   *
   * @param commission charge per transaction
   */
  void setCommissionCharge(Double commission);

  /**
   * Fetches the money invested in a flexible portfolio by a given date.
   *
   * @param portfolioName name of flexible portfolio
   * @param date          by which cost basis needs to be calculated
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @return money invested by given date in this portfolio
   */
  Double getCostBasisForFlexiblePortfolio(String portfolioName, String date,
      Map<String, Map<String, Double>> stockPriceMap);

  /**
   * Fetches the composition of a portfolio by its name if it exists with the user.
   *
   * @param portfolioName name of the portfolio to get composition for
   * @return map of stock name and quantity in the portfolio
   * @throws IllegalArgumentException if portfolio with portfolioName does not exist, or blank
   *                                  portfolioName is given
   */
  Map<String, Double> getCompositionOfPortfolio(String portfolioName, String date)
      throws IllegalArgumentException;

  /**
   * Fetches the list of all inflexible portfolios for the user.
   *
   * @return a list of names of portfolios that are present with the user
   */
  List<String> listAllInflexiblePortfolioNames();

  /**
   * Fetches the values of stocks of a portfolio on a given date that is with the user.
   *
   * @param portfolioName represents name of the portfolio whose value is asked
   * @param stockValues   map of maps that represents stocks and their prices on a range of dates
   * @param date          for which value is asked
   * @return map of stock names and their values on the given date that is a product of quantity of
   *         the stock in the portfolio and its price on the given date
   * @throws IllegalArgumentException if portfolio does not exist with the given name or stock
   *                                  values does not have the data for stock in the portfolio for
   *                                  which value needs to be calculated on the given date
   * @throws NullPointerException     if portfolio name, stock values or date are null
   */
  Double getValuesOfStocksOfPortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockValues, String date)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Fetches the list of all flexible portfolios for the user.
   *
   * @return a list of names of portfolios that are present with the user
   */
  List<String> listAllFlexiblePortfolioNames();

  /**
   * Checks if user has inflexible portfolio with name as given.
   *
   * @param portfolioName to be checked for existence with the user
   * @return true if portfolio exists with the name given, false otherwise
   */
  boolean inflexiblePortfolioExists(String portfolioName);

  /**
   * Checks if user has flexible portfolio with name as given.
   *
   * @param portfolioName to be checked for existence with the user
   * @return true if portfolio exists with the name given, false otherwise
   */
  boolean flexiblePortfolioExists(String portfolioName) throws NullPointerException;

  /**
   * Saves a portfolio that exists with the user onto a file.
   *
   * @param portfolioName name of the portfolio
   * @param filePath      path of file where portfolio needs to be saved
   * @param fileUtil      helps take data of portfolio and performs file IO operation to write the
   *                      file
   */
  void savePortfolio(String portfolioName, String filePath, FileUtil fileUtil);

  /**
   * Fetches the data from existing portfolios in the files.
   *
   * @param fileUtil that enable File read operation for various portfolio files
   * @param filePath path of files from which to load existing portfolios
   * @return contents of the file as a list of string arrays represnting each line of the file
   */
  Map<String, List<String[]>> loadExistingPortfolio(FileUtil fileUtil, String filePath);

  /**
   * Fetches the performance of a flexible portfolio on a given range of dates.
   *
   * @param portfolioName     name of flexible portfolio
   * @param from              a date user wants to see performance
   * @param to                a date upto which user wants to see performance
   * @param stockPriceMap     a map of maps of stock names and their respective values on a range of
   *                          dates
   * @param performanceScaler that takes in values of portfolio and scales suitably for view to
   *                          show
   * @throws IllegalArgumentException when value of a portfolio cannot be obtained from last 100
   *                                  days
   */
  void getPerformanceOfFlexiblePortfolio(String portfolioName, String from, String to,
      Map<String, Map<String, Double>> stockPriceMap,
      BiConsumer<Map<String, Double>, ApiPeriod> performanceScaler) throws IllegalArgumentException;

  /**
   * Adds trades that can be divided according to the ratios provided and within an amount into the
   * given portfolio on a range of dates that can be called as investing by a strategy.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param amount        of money to be traded
   * @param tickerRatios  a map of proportion of stocks in which maount will be divided
   * @param from          date from which to invest
   * @param to            date upto which to invest
   * @param period        frequency of investment could be weekly or monthly
   * @throws IllegalArgumentException if stock price data for a stock in the startegy is not
   *                                  available on a date.
   */
  void addStrategyToFlexiblePortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String from, String to, ApiPeriod period)
      throws IllegalArgumentException;
}
