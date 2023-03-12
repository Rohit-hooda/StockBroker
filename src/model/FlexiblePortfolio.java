package model;

import java.util.Map;
import java.util.function.BiConsumer;
import util.ApiPeriod;

/**
 * Represents a flexible version of portfolio that can perform buy or sell of stocks and show its
 * cost basis and performance on given date.
 */
interface FlexiblePortfolio extends Portfolio {

  /**
   * Adds a buy or sell of stock on a given date in the portfolio.
   *
   * @param ticker   name of the stock
   * @param quantity positive integer if it is a buy or negative integer if it is a sell of stock
   * @param date     of trade
   * @throws IllegalArgumentException if trade requested to add is not consistent with previously
   *                                  performed trades
   */
  void addTrade(String ticker, Double quantity, String date) throws IllegalArgumentException;

  /**
   * Fetches the money invested in this portfolio by a given date that include all purchases and
   * commission charge incurred per transaction.
   *
   * @param date          by which to determine cost basis
   * @param commission    charge per transaction
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @return money invested by given date in this portfolio
   * @throws IllegalArgumentException when certain stock does not have data for its price in last
   *                                  100 days
   */
  Double getCostBasis(String date, Double commission,
      Map<String, Map<String, Double>> stockPriceMap) throws IllegalArgumentException;

  /**
   * Fetches the performance of flexible portfolio on a given range of dates.
   *
   * @param from              a date user wants to see performance
   * @param to                a date upto which user wants to see performance
   * @param stockPriceMap     a map of maps of stock names and their respective values on a range of
   *                          dates
   * @param performanceScaler that takes in values of portfolio and scales suitably for view to
   *                          show
   * @throws IllegalArgumentException when value of a portfolio cannot be obtained from last 100
   *                                  days
   */
  void getPerformance(String from, String to,
      Map<String, Map<String, Double>> stockPriceMap,
      BiConsumer<Map<String, Double>, ApiPeriod> performanceScaler) throws IllegalArgumentException;

  /**
   * Adds trades that can be divided according to the ratios provided and within an amount into the
   * portfolio.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param amount        of money to be traded
   * @param tickerRatios  a map of proportion of stocks in which maount will be divided
   * @param date          on which to perform this trade of multiple stocks
   * @throws IllegalArgumentException when sum of proportions is not 100 or when price for the given
   *                                  date is not available
   */
  void addFractionalTrades(Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String date)
      throws IllegalArgumentException, NullPointerException;

  /**
   * Adds trades that can be divided according to the ratios provided and within an amount into the
   * portfolio on a range of dates that can be called as investing by a strategy.
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
  void addStrategy(Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String from, String to, ApiPeriod period)
      throws IllegalArgumentException;
}
