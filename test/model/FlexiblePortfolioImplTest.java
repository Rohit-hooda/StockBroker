package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import org.junit.Before;
import org.junit.Test;
import util.ApiPeriod;
import util.FileUtil;

/**
 * Represents a test class for {@link FlexiblePortfolioImpl}.
 */
public class FlexiblePortfolioImplTest {

  private Map<String, Map<String, Double>> stockPriceMap;
  private FlexiblePortfolio flexiblePortfolio;

  @Before
  public void setUp() throws Exception {
    String date1 = "2022-11-08";
    String date2 = "2022-11-09";
    String date3 = "2022-11-10";
    stockPriceMap = new HashMap<>();
    Map<String, Double> sp1 = new HashMap<>();
    sp1.put(date1, 1.0);
    sp1.put(date2, 2.0);
    sp1.put(date3, 3.0);
    stockPriceMap.put("Stock1_DAILY", sp1);
    Map<String, Double> sp2 = new HashMap<>();
    sp2.put(date1, 1.0);
    sp2.put(date2, 2.0);
    sp2.put(date3, 3.0);
    stockPriceMap.put("Stock2_DAILY", sp2);
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2022-11-08");
    flexiblePortfolio.addTrade("Stock1", 13.0, "2022-11-10");
    flexiblePortfolio.addTrade("Stock2", 22.0, "2022-11-08");
    flexiblePortfolio.addTrade("Stock2", -22.0, "2022-11-09");
    flexiblePortfolio.addTrade("Stock2", 10.0, "2022-11-10");
  }

  @Test
  public void testAddFractionalTrade() {
    Map<String, Double> prevComposition = flexiblePortfolio.getComposition("2022-11-09");
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
    Map<String, Double> composition = flexiblePortfolio.getComposition("2022-11-09");
    for (Map.Entry<String, Double> entry : composition.entrySet()) {
      assertEquals(prevComposition.get(entry.getKey())
          + (tickerRatios.get(entry.getKey()) / 2), entry.getValue(), 0.0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithAmountLessThanZero() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, -100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithProportionSumLessThan100() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 10.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithProportionSumGreaterThan100() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 50.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithNegativeProportion() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", -50.0);
    tickerRatios.put("Stock2", 150.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithZeroProportion() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 0.0);
    tickerRatios.put("Stock2", 100.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithFutureDate() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2023-10-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithInvalidDate() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2023-90-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithPriceNotAvailableOnDate() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2021-10-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddFractionalTradeWithStockPriceNotAvailable() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock3", 70.0);
    flexiblePortfolio.addFractionalTrades(stockPriceMap, 100.0, tickerRatios,
        "2022-11-09");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStrategyWithAmountLessThanZero() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, -100.0, tickerRatios,
        "2021-11-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithProportionSumLessThan100() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 10.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-11-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithProportionSumGreaterThan100() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 50.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-11-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithNegativeProportion() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", -50.0);
    tickerRatios.put("Stock2", 150.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-11-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithZeroProportion() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 0.0);
    tickerRatios.put("Stock2", 100.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-11-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithFutureDate() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-10-09", "2023-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithInvalidDate() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2021-90-09", "2022-11-09", ApiPeriod.MONTHLY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrategyWithInvalidFromToDates() {
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    flexiblePortfolio.addStrategy(stockPriceMap, 100.0, tickerRatios,
        "2022-10-10", "2021-11-09", ApiPeriod.MONTHLY);
  }

  @Test
  public void testStrategyWeeklyWithoutToDate() {
    String to = "2022-11-01";
    String from = "2022-09-01";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new TreeMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      if (fromD.getDayOfWeek().getValue() > 5) {
        fromD = fromD.plusDays(1);
        continue;
      }
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPrices.remove("2022-09-08");
    stockPrices.remove("2022-09-09");
    stockPrices.remove("2022-09-12");
    stockPrices.remove("2022-09-13");
    stockPrices.remove("2022-09-14");
    stockPrices.remove("2022-09-15");
    stockPriceMap = new HashMap<>();
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 50.0);
    tickerRatios.put("Stock2", 50.0);
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addStrategy(stockPriceMap, 2000.0, tickerRatios, "2022-09-01", "",
        ApiPeriod.WEEKLY);

    StringBuilder actualValues = new StringBuilder();
    String expected = getExpectedOutputForWeeklyStrategy();
    for (Map.Entry<String, Double> stockPrice : stockPrices.entrySet()) {
      actualValues.append(flexiblePortfolio.getValue(stockPriceMap, stockPrice.getKey()));
      actualValues.append("\n");
    }

    assertEquals(expected, actualValues.toString());
  }

  @Test
  public void testStrategyWeekly() {
    String to = "2022-11-01";
    String from = "2022-09-01";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new TreeMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      if (fromD.getDayOfWeek().getValue() > 5) {
        fromD = fromD.plusDays(1);
        continue;
      }
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPrices.remove("2022-09-08");
    stockPrices.remove("2022-09-09");
    stockPrices.remove("2022-09-12");
    stockPrices.remove("2022-09-13");
    stockPrices.remove("2022-09-14");
    stockPrices.remove("2022-09-15");
    stockPriceMap = new HashMap<>();
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 50.0);
    tickerRatios.put("Stock2", 50.0);
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addStrategy(stockPriceMap, 2000.0, tickerRatios, "2022-09-01", "2022-11-01",
        ApiPeriod.WEEKLY);

    StringBuilder actualValues = new StringBuilder();
    String expected = getExpectedOutputForWeeklyStrategy();
    for (Map.Entry<String, Double> stockPrice : stockPrices.entrySet()) {
      actualValues.append(flexiblePortfolio.getValue(stockPriceMap, stockPrice.getKey()));
      actualValues.append("\n");
    }

    assertEquals(expected, actualValues.toString());
  }

  private String getExpectedOutputForWeeklyStrategy() {
    return "2000.0\n"
        + "2200.0\n"
        + "2400.0000000000005\n"
        + "2600.0000000000005\n"
        + "2800.000000000001\n"
        + "6200.000000000002\n"
        + "6495.238095238097\n"
        + "6790.476190476193\n"
        + "7085.714285714288\n"
        + "9380.952380952383\n"
        + "9756.190476190479\n"
        + "10131.428571428574\n"
        + "10506.66666666667\n"
        + "10881.904761904765\n"
        + "13257.14285714286\n"
        + "13699.047619047622\n"
        + "14140.952380952385\n"
        + "14582.857142857147\n"
        + "15024.761904761908\n"
        + "17466.66666666667\n"
        + "17965.714285714294\n"
        + "18464.761904761912\n"
        + "18963.80952380953\n"
        + "19462.85714285715\n"
        + "21961.90476190477\n"
        + "22510.95238095239\n"
        + "23060.000000000004\n"
        + "23609.047619047622\n"
        + "24158.095238095237\n"
        + "26707.142857142855\n"
        + "27300.634920634915\n"
        + "27894.12698412698\n"
        + "28487.61904761904\n"
        + "29081.1111111111\n"
        + "31674.603174603162\n"
        + "32308.095238095222\n"
        + "32941.587301587286\n"
        + "33575.07936507934\n";
  }

  @Test
  public void testStrategyMonthly() {
    String from = "2021-11-01";
    String to = "2022-11-01";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new TreeMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      if (fromD.getDayOfWeek().getValue() > 5) {
        fromD = fromD.plusDays(1);
        continue;
      }
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    double amount = 2000.0;
    flexiblePortfolio.addStrategy(stockPriceMap, amount, tickerRatios,
        "2021-11-25", "2022-11-01",
        ApiPeriod.MONTHLY);

    StringBuilder actualValues = new StringBuilder();
    String expectedValues = getExpectedOutputForMonthlyStrategy();
    double commission = 10.0;
    double investmentTillFirstTwoMonths = 2 * amount + 2 * 2 * commission;
    for (int i = 1; i < 10; i++) {

      assertEquals(investmentTillFirstTwoMonths + i * amount + (2 * i * commission),
          flexiblePortfolio.getCostBasis("2022-0" + i + "-28", 10.0, stockPriceMap),
          0.01);

      actualValues.append(flexiblePortfolio.getValue(stockPriceMap, "2022-0" + i + "-28"));
      actualValues.append("\n");
    }

    assertEquals(expectedValues, actualValues.toString());
  }

  @Test
  public void testStrategyMonthlyAfterAddingBuyingStocks() {
    String from = "2021-11-01";
    String to = "2022-11-01";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new TreeMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      if (fromD.getDayOfWeek().getValue() > 5) {
        fromD = fromD.plusDays(1);
        continue;
      }
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);
    Map<String, Double> tickerRatios = new HashMap<>();
    tickerRatios.put("Stock1", 30.0);
    tickerRatios.put("Stock2", 70.0);
    double amount = 2000.0;
    flexiblePortfolio.addTrade("Stock1", 10.0, "2021-11-02");
    flexiblePortfolio.addTrade("Stock2", 20.0, "2021-11-22");
    flexiblePortfolio.addStrategy(stockPriceMap, amount, tickerRatios,
        "2021-11-25", "2022-11-01",
        ApiPeriod.MONTHLY);

    double commission = 10.0;

    assertEquals(0.0, flexiblePortfolio.getCostBasis("2021-11-01", commission, stockPriceMap),
        0.01);
    assertEquals(21.0, flexiblePortfolio.getCostBasis("2021-11-03", commission, stockPriceMap),
        0.01);
    assertEquals(81.0, flexiblePortfolio.getCostBasis("2021-11-23", commission, stockPriceMap),
        0.01);
    assertEquals(2101.0, flexiblePortfolio.getCostBasis("2021-11-26", commission, stockPriceMap),
        0.01);
    assertEquals(4121.0, flexiblePortfolio.getCostBasis("2021-12-28", commission, stockPriceMap),
        0.01);
    assertEquals(6141.0, flexiblePortfolio.getCostBasis("2022-01-25", commission, stockPriceMap),
        0.01);
  }

  private String getExpectedOutputForMonthlyStrategy() {
    return "10330.221327967794\n"
        + "15283.047219487113\n"
        + "20518.07470429141\n"
        + "26666.134089594132\n"
        + "32749.501766756126\n"
        + "39291.9904808147\n"
        + "46097.81252820661\n"
        + "52875.520365127995\n"
        + "60321.029790373985\n";
  }

  @Test
  public void testCreateFlexiblePortfolio() {
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");

    assertEquals(0, flexiblePortfolio.getValue(stockPriceMap, "2022-11-10"), 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateFlexiblePortfolioWithNullName() {
    flexiblePortfolio = new FlexiblePortfolioImpl(null);
  }

  @Test
  public void getValue() {
    Double actual1 = flexiblePortfolio.getValue(stockPriceMap, "2022-11-08");
    Double actual2 = flexiblePortfolio.getValue(stockPriceMap, "2022-11-09");
    Double actual3 = flexiblePortfolio.getValue(stockPriceMap, "2022-11-10");

    assertEquals(34.0, actual1, 0.0);
    assertEquals(24.0, actual2, 0.0);
    assertEquals(105.0, actual3, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowWhenGetValueOnInvalidDate() {
    stockPriceMap.put("Stock1_DAILY", new HashMap<>());
    Double actual1 = flexiblePortfolio.getValue(stockPriceMap, "2022-11-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenAddInconsistentTrade() {
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2022-11-08");
    flexiblePortfolio.addTrade("Stock1", -13.0, "2022-11-10");
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenAddTradeWithNullDate() {
    flexiblePortfolio.addTrade("Stock1", 2.0, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenAddTradeWithNullQuantity() {
    flexiblePortfolio.addTrade("Stock1", null, "2022-10-10");
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenAddTradeWithNullTicker() {
    flexiblePortfolio.addTrade(null, 2.0, "2022-10-10");
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenAddTradeWithNullValues() {
    flexiblePortfolio.addTrade(null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOnNullDate() {
    stockPriceMap.put("Stock1_DAILY", new HashMap<>());
    Double actual1 = flexiblePortfolio.getValue(stockPriceMap, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOnNullStockPriceMap() {
    stockPriceMap.put("Stock1_DAILY", new HashMap<>());
    Double actual1 = flexiblePortfolio.getValue(null, "2022-11-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenGetValueOnDataNotPresentInStockPriceMap() {
    stockPriceMap.put("Stock1_DAILY", new HashMap<>());
    Double actual1 = flexiblePortfolio.getValue(stockPriceMap, "2022-11-18");
  }

  @Test
  public void addTradeAndGetComposition() {
    Map<String, Double> stocks = flexiblePortfolio.getComposition("2022-11-10");

    assertEquals(25.0, stocks.get("Stock1"), 0.0);
    assertEquals(10.0, stocks.get("Stock2"), 0.0);
  }

  @Test
  public void getCostBasis() {

    assertEquals(54.0, flexiblePortfolio.getCostBasis("2022-11-08", 10.0, stockPriceMap), 0.0);
    assertEquals(64.0, flexiblePortfolio.getCostBasis("2022-11-09", 10.0, stockPriceMap), 0.0);
    assertEquals(153.0, flexiblePortfolio.getCostBasis("2022-11-10", 10.0, stockPriceMap), 0.0);
  }

  @Test
  public void getCostBasisInEmptyFlexiblePortfolio() {
    flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");

    assertEquals(0, flexiblePortfolio.getCostBasis("2022-11-08", 10.0, stockPriceMap), 0.0);
  }

  @Test
  public void getDailyPerformance() {
    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    flexiblePortfolio.getPerformance("2022-11-07", "2022-11-10", stockPriceMap,
        mockLog);
    String expected = "DAILY\n"
        + "2022-11-07,0.0\n"
        + "2022-11-08,34.0\n"
        + "2022-11-09,24.0\n"
        + "2022-11-10,105.0\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getDailyPerformanceWithValueForDateMissing() {
    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    Map<String, Double> sp1 = new HashMap<>();
    sp1.put("2022-11-07", 1.0);
    sp1.put("2022-11-08", 1.0);
    sp1.put("2022-11-10", 1.0);
    stockPriceMap.put("Stock1_DAILY", sp1);
    stockPriceMap.put("Stock2_DAILY", sp1);
    flexiblePortfolio.getPerformance("2022-11-07", "2022-11-10", stockPriceMap,
        mockLog);
    String expected = "DAILY\n"
        + "2022-11-07,0.0\n"
        + "2022-11-08,34.0\n"
        + "2022-11-09,12.0\n"
        + "2022-11-10,35.0\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getWeeklyPerformance() {
    String to = "2022-11-11";
    String from = "2022-10-07";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new HashMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);

    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2022-09-08");
    flexiblePortfolio.addTrade("Stock1", 13.0, "2022-10-10");
    flexiblePortfolio.addTrade("Stock2", 22.0, "2022-10-18");
    flexiblePortfolio.addTrade("Stock2", -22.0, "2022-10-25");
    flexiblePortfolio.addTrade("Stock2", 10.0, "2022-10-26");

    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    flexiblePortfolio.getPerformance("2022-10-07", "2022-11-11", stockPriceMap, mockLog);
    String expected = "WEEKLY\n"
        + "2022-10-07,12.0\n"
        + "2022-10-14,42.500000000000014\n"
        + "2022-10-21,112.80000000000005\n"
        + "2022-10-28,108.50000000000006\n"
        + "2022-11-04,133.00000000000009\n"
        + "2022-11-11,157.50000000000003\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getWeeklyPerformanceWithValuesNotPresentForFridays() {
    String to = "2022-11-11";
    String from = "2022-10-07";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new HashMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      if (fromD.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
        fromD = fromD.plusDays(1);
        continue;
      }
      String today = fromD.format(formatter);
      stockPrices.put(today, price);
      price += 0.1;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);

    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2022-09-08");
    flexiblePortfolio.addTrade("Stock1", 13.0, "2022-10-10");
    flexiblePortfolio.addTrade("Stock2", 22.0, "2022-10-18");
    flexiblePortfolio.addTrade("Stock2", -22.0, "2022-10-25");
    flexiblePortfolio.addTrade("Stock2", 10.0, "2022-10-26");

    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    flexiblePortfolio.getPerformance("2022-10-06", "2022-11-11", stockPriceMap, mockLog);
    String expected = "WEEKLY\n"
        + "2022-10-07,0.0\n"
        + "2022-10-14,37.500000000000014\n"
        + "2022-10-21,98.70000000000005\n"
        + "2022-10-28,94.50000000000006\n"
        + "2022-11-04,115.50000000000009\n"
        + "2022-11-11,136.5000000000001\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getMonthlyPerformance() {
    String to = "2022-10-07";
    String from = "2021-10-07";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new HashMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      String today = fromD.format(formatter);
      stockPrices.put(today, price);
      price += 0.01;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);

    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2021-11-08");
    flexiblePortfolio.addTrade("Stock1", 13.0, "2021-12-10");
    flexiblePortfolio.addTrade("Stock2", 22.0, "2022-01-18");
    flexiblePortfolio.addTrade("Stock2", -22.0, "2022-04-25");
    flexiblePortfolio.addTrade("Stock2", 10.0, "2022-10-06");

    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    flexiblePortfolio.getPerformance("2021-11-08", "2022-10-06", stockPriceMap, mockLog);
    String expected = "MONTHLY\n"
        + "2021-11-30,18.480000000000004\n"
        + "2021-12-31,46.25000000000002\n"
        + "2022-01-31,101.51999999999988\n"
        + "2022-02-28,114.67999999999961\n"
        + "2022-03-31,129.2499999999993\n"
        + "2022-04-30,76.24999999999946\n"
        + "2022-05-31,83.9999999999993\n"
        + "2022-06-30,91.49999999999913\n"
        + "2022-07-31,99.24999999999898\n"
        + "2022-08-31,106.9999999999988\n"
        + "2022-09-30,114.49999999999865\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getMonthlyPerformanceWhenPriceNotPresentOnLastDateOfMonth() {
    String to = "2022-10-07";
    String from = "2021-10-07";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fromD = LocalDate.parse(from, formatter);
    LocalDate toD = LocalDate.parse(to, formatter);
    Map<String, Double> stockPrices = new HashMap<>();
    Double price = 1.0;
    while (!fromD.isAfter(toD)) {
      if (fromD.getDayOfMonth() == 30 || fromD.getDayOfMonth() == 31) {
        fromD = fromD.plusDays(1);
        continue;
      }
      String today = fromD.format(formatter);
      stockPrices.put(today, price);
      price += 0.01;
      fromD = fromD.plusDays(1);
    }
    stockPriceMap.put("Stock1_DAILY", stockPrices);
    stockPriceMap.put("Stock2_DAILY", stockPrices);

    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolioImpl("Portfolio1");
    flexiblePortfolio.addTrade("Stock1", 12.0, "2021-11-08");
    flexiblePortfolio.addTrade("Stock1", 13.0, "2021-12-10");
    flexiblePortfolio.addTrade("Stock2", 22.0, "2022-01-18");
    flexiblePortfolio.addTrade("Stock2", -22.0, "2022-04-25");
    flexiblePortfolio.addTrade("Stock2", 10.0, "2022-10-06");

    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    flexiblePortfolio.getPerformance("2021-09-08", "2022-10-06", stockPriceMap, mockLog);
    String expected = "MONTHLY\n"
        + "2021-09-30,0.0\n"
        + "2021-10-31,0.0\n"
        + "2021-11-30,18.120000000000005\n"
        + "2021-12-31,45.000000000000014\n"
        + "2022-01-31,98.22999999999995\n"
        + "2022-02-28,111.38999999999967\n"
        + "2022-03-31,125.01999999999938\n"
        + "2022-04-30,73.74999999999952\n"
        + "2022-05-31,80.99999999999936\n"
        + "2022-06-30,88.2499999999992\n"
        + "2022-07-31,95.49999999999905\n"
        + "2022-08-31,102.74999999999889\n"
        + "2022-09-30,109.99999999999875\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void getPortfolioName() {
    String actual = flexiblePortfolio.getPortfolioName();

    assertEquals("Portfolio1", actual);
  }

  @Test
  public void checkEqualPortfolios() {
    Portfolio portfolio = new FlexiblePortfolioImpl("Portfolio1");
    Portfolio portfolioCopy = new FlexiblePortfolioImpl("Portfolio1");

    assertNotSame(portfolio, portfolioCopy);
    assertEquals(portfolio.hashCode(), portfolioCopy.hashCode());
    assertEquals(portfolio, portfolioCopy);
  }

  @Test
  public void checkUnequalPortfolios() {
    assertNotEquals(null, flexiblePortfolio);
  }


  @Test
  public void savePortfolio() {
    StringBuilder log = new StringBuilder();
    FileUtil mockFileUtil = new MockFileUtil(log);
    flexiblePortfolio.savePortfolio("filePath", mockFileUtil);

    String expected = "Portfolio1\n"
        + "filePath\n"
        + "Stock Name,Quantity,Date,Type of Trade,\n"
        + "Stock1,12.0,2022-11-08,BUY,\n"
        + "Stock2,22.0,2022-11-08,BUY,\n"
        + "Stock2,22.0,2022-11-09,SELL,\n"
        + "Stock1,13.0,2022-11-10,BUY,\n"
        + "Stock2,10.0,2022-11-10,BUY,\n";

    assertEquals(expected, log.toString());
  }

  static class MockFileUtil implements FileUtil {

    private final StringBuilder log;

    MockFileUtil(StringBuilder log) {
      this.log = log;
    }

    @Override
    public List<String[]> readFile(String filePath) throws IllegalArgumentException {
      return null;
    }

    @Override
    public Map<String, List<String[]>> readAllFiles(String filePath) {
      return null;
    }

    @Override
    public void savePortfolio(List<String[]> stocks, String portfolioName,
        String resourceDirectory) {
      log.append(portfolioName);
      log.append("\n");
      log.append(resourceDirectory);
      log.append("\n");
      for (String[] strArr : stocks) {
        for (String str : strArr) {
          log.append(str);
          log.append(",");
        }
        log.append("\n");
      }
    }
  }

  class MockPerformanceScalerCommand implements BiConsumer<Map<String, Double>, ApiPeriod> {

    private final StringBuilder log;

    MockPerformanceScalerCommand(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void accept(Map<String, Double> performances, ApiPeriod apiPeriod) {
      log.append(apiPeriod.getApiPeriod());
      log.append("\n");
      for (Map.Entry<String, Double> performance : performances.entrySet()) {
        log.append(performance.getKey());
        log.append(",");
        log.append(performance.getValue());
        log.append("\n");
      }
    }
  }
}