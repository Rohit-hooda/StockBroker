package util;

import static org.junit.Assert.assertEquals;

import java.util.function.BiFunction;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link StringAppender} util class is correct.
 */
public class StringAppenderTest {

  @Test
  public void apply() {
    BiFunction<String[], String, String> stringAppender = new StringAppender();
    String[] stockDataArray = {"Ticker", "Quantity", "StockValue"};
    String actual = stringAppender.apply(stockDataArray, ",");

    assertEquals("Ticker,Quantity,StockValue", actual);
  }
}