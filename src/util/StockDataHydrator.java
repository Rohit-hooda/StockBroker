package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class makes the API call to the AlphaVantage API and saves the data to the file.
 */

public final class StockDataHydrator implements DataHydrator {

  private final String apiKey;
  private final String stockPriceDataDirectory;
  private final ApiPeriod period;

  /**
   * Constructs an API hydrator instance that can make an api call and write to a file.
   *
   * @param apiKey                  to make an api call
   * @param stockPriceDataDirectory directory where file needs to be saved
   * @param period                  fetch DAILY, WEEKLY or MONTHLY data of stock
   */
  public StockDataHydrator(String apiKey, String stockPriceDataDirectory,
      ApiPeriod period) {
    this.apiKey = apiKey;
    this.stockPriceDataDirectory = stockPriceDataDirectory;
    this.period = period;
  }

  @Override
  public void hydrate(String stockName) {

    URL url;
    try {
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_"
          + period.getApiPeriod()
          + "&outputsize=full"
          + "&symbol"
          + "=" + stockName + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the EOD Historical API has either changed or "
          + "no longer works");
    }

    InputStream in;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
      writeStockData(output.toString(), stockName + "_" + period.getApiPeriod(),
          stockPriceDataDirectory);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockName);
    }
  }

  private void writeStockData(String content, String stockFileName, String stockPriceDataDirectory)
      throws IOException {
    FileWriter file = new FileWriter(stockPriceDataDirectory + stockFileName + ".csv");
    file.write(String.valueOf(content));
    file.close();
  }
}
