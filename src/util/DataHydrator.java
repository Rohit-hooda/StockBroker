package util;

/**
 * This interface hydrates all the data required to initialize and run this application.
 */
public interface DataHydrator {

  /**
   * Fetches the historical data from an API for a stock name.
   *
   * @param stockName name of the stock
   */
  void hydrate(String stockName);
}
