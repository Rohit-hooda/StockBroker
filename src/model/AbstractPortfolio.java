package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Consumer;
import util.ApiPeriod;
import util.ParamNullChecker;

/**
 * Represents an abstraction for Portfolio class that contains common features for all kinds of
 * portfolios.
 */
abstract class AbstractPortfolio implements Portfolio {

  private final Consumer<Object[]> nullChecker = new ParamNullChecker();

  @Override
  public Double getValue(Map<String, Map<String, Double>> stockPriceMap, String date)
      throws IllegalArgumentException, NullPointerException {
    nullChecker.accept(new Object[]{stockPriceMap, date});
    Map<String, Double> composition = this.getComposition(date);
    double portfolioValue = 0.0;
    for (Map.Entry<String, Double> stock : composition.entrySet()) {
      String ticker = stock.getKey();
      portfolioValue += getStockPriceOnDate(ticker, date, stockPriceMap) * stock.getValue();
    }
    return portfolioValue;
  }

  /**
   * Fetches the stock price of a stock on a given date.
   *
   * @param ticker        is the stock name
   * @param date          on which its price is asked
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @return price of stock on a given date
   */
  protected Double getStockPriceOnDate(String ticker, String date,
      Map<String, Map<String, Double>> stockPriceMap) {
    String tickerAndPeriod = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
    if (stockPriceMap.containsKey(tickerAndPeriod)) {
      Map<String, Double> stockValuesHistory = stockPriceMap.get(tickerAndPeriod);
      if (stockValuesHistory.containsKey(date)) {
        return stockValuesHistory.get(date);
      } else {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        for (int i = 0; i < 100; i++) {
          LocalDate current = localDate.minusDays(i);
          String today = current.format(formatter);
          if (stockValuesHistory.containsKey(today)) {
            return stockValuesHistory.get(today);
          }
        }
        throw new IllegalArgumentException(
            "Stock values not present on any date in last 100 days from the given date");
      }
    } else {
      throw new IllegalArgumentException("Stock values not present for given stock name");
    }
  }
}
