package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an implementation of {@link StockPriceUtil} that has functionalities to be performed.
 */

public final class StockPriceUtil implements StockUtil {

  private static final String STOCK_PRICE_DATA_DIRECTORY = "res/stock_value_data/";
  private static final String CSV = ".csv";

  @Override
  public Map<String, Map<String, Double>> fetchStockData(String apiKey,
      String stockName,
      Map<String, Map<String, Double>> stockPriceMap) {
    String stockDataFileName = stockName + "_" + ApiPeriod.DAILY.getApiPeriod();
    new StockDataHydrator(apiKey, STOCK_PRICE_DATA_DIRECTORY, ApiPeriod.DAILY).hydrate(stockName);
    List<String[]> stockDataFromFile = new CsvFileUtil().readFile(
        STOCK_PRICE_DATA_DIRECTORY + stockDataFileName + CSV);
    Map<String, Double> stockData = getStockMap(stockDataFromFile, 0, 4);
    stockPriceMap.put(stockDataFileName, stockData);

    return stockPriceMap;
  }

  @Override
  public Map<String, Double> getStockMap(List<String[]> stocks, int tickerColumn,
      int valueColumn) {
    Map<String, Double> stockMap = new HashMap<>();
    for (String[] stock : stocks) {
      Double value = Double.parseDouble(stock[valueColumn]);
      stockMap.put(stock[tickerColumn], value);
    }
    return stockMap;
  }

  @Override
  public Map<String, Map<String, Double>> fetchAllStockData(String apiKey, Set<String> tickerSet,
      Map<String, Map<String, Double>> stockPriceMap) {
    for (String stock : tickerSet) {
      if (!stockPriceMap.containsKey(stock + "_" + ApiPeriod.DAILY.getApiPeriod())) {
        stockPriceMap = fetchStockData(apiKey, stock,
            stockPriceMap);
      }
    }
    return stockPriceMap;
  }
}
