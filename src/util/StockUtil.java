package util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interface gives various functionalities that can be performed on a stock.
 */
public interface StockUtil {

  /**
   * Fetches the data of the stock from the AlphaVantage API.
   *
   * @param apiKey                 is the api key provided to the AlphaVantage API
   * @param stockPriceDataFileName is the file name of the stock file
   * @param stockPriceMap          is the price map which is hydrated by the API call
   * @return returns a map of the stock data
   */
  Map<String, Map<String, Double>> fetchStockData(String apiKey, String stockPriceDataFileName,
      Map<String, Map<String, Double>> stockPriceMap);

  /**
   * Fetches the value and the ticker of the stock.
   *
   * @param stocks       is the list of stocks
   * @param tickerColumn is the ticker of the stock
   * @param valueColumn  is the value of the stock
   * @return the map of ticker and value of the stock
   */
  Map<String, Double> getStockMap(List<String[]> stocks, int tickerColumn,
      int valueColumn);

  /**
   * Fetches all the stock data.
   *
   * @param apiKey        is the api key provided to the AlphaVantage API
   * @param tickerSet     is the ticker set
   * @param stockPriceMap is the value ticker map for a stock
   * @return the map of stock price map
   */
  Map<String, Map<String, Double>> fetchAllStockData(String apiKey, Set<String> tickerSet,
      Map<String, Map<String, Double>> stockPriceMap);
}
