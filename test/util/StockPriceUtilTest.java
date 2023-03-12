package util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link StockPriceUtil} util class is correct.
 */
public class StockPriceUtilTest {

  @Test
  public void getStockMap() {
    StockUtil stockPriceUtil = new StockPriceUtil();
    List<String[]> input = new ArrayList<>();
    input.add(new String[]{"Stock1", "1.0"});
    input.add(new String[]{"Stock2", "2.0"});
    input.add(new String[]{"Stock3", "3.0"});
    Map<String, Double> stockMap = stockPriceUtil.getStockMap(input, 0, 1);

    for (int i = 1; i < 4; i++) {
      assertEquals(i, stockMap.get("Stock" + i), 0.0);
    }
  }
}