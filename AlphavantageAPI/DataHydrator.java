import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DataHydrator {

  public static void main(String[] args) throws IOException, InterruptedException {

    //the API key needed to use this web service.
    //Please get your own free API key here: https://www.alphavantage.co/
    //Please look at documentation here: https://www.alphavantage.co/documentation/
    String apiKey = "PBBOCARQLUUHKQBA";

//    String[] tickers = {"GOOG_DAILY", "AMZN_DAILY", "MSFT_DAILY", "AAPL_DAILY", "TSLA_DAILY", "INTC_DAILY", "ADBE_DAILY", "META_DAILY", "AGND_DAILY",
//        "NFLX_DAILY"};
    String[] tickers = {"GOOG_DAILY", "AMZN_DAILY", "MSFT_DAILY", "AAPL_DAILY", "TSLA_DAILY"};

    for (String ticker : tickers) {
      accept(apiKey, ticker);
    }
  }

  public static void accept(String apiKey, String stockFileName) {
    String[] tickerAndPeriod = stockFileName.split("_");
    String ticker = tickerAndPeriod[0];
    String period = tickerAndPeriod[1];
    URL url = null;
    try {
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_"
          + period
          + "&outputsize=full"
          + "&symbol"
          + "=" + ticker + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the EOD Historical API has either changed or "
          + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
      writeStockData(output.toString(), stockFileName);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }
  }

  private static void writeStockData(String content, String stockFileName) throws IOException {
    FileWriter file = new FileWriter("resources/stock_value_data_cache/" + stockFileName + ".csv");
    file.write(String.valueOf(content));
    file.close();
  }
}
