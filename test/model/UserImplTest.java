package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import org.junit.Before;
import org.junit.Test;
import util.ApiPeriod;
import util.FileUtil;

/**
 * Represents a Test class to test if behaviour of {@link UserImpl} is correct.
 */
public class UserImplTest {

  private static final String portfolio1Name = "Portfolio 1";
  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
  private User user;
  private List<Stock> stocks;
  private Map<String, Double> stockMap;
  private Map<String, Map<String, Double>> stockPriceMap;
  private String date;
  private String date2;
  private String date3;
  private String stock1Name;
  private String stock2Name;

  @Before
  public void setUp() throws ParseException {
    user = new UserImpl();
    stock1Name = "Stock1";
    stock2Name = "Stock2";
    prepareStockMap(stock1Name);
    date = "2022-10-10";
    date2 = "2022-10-11";
    date3 = "2022-10-12";
    prepareStockPriceMap(stock1Name, stock2Name);
  }

  private void prepareStockMap(String stock1Name) {
    Stock stock1 = new MockStock(stock1Name);
    stocks = new ArrayList<>();
    stocks.add(stock1);
    stockMap = new HashMap<>();
    stockMap.put(stock1Name, 2.0);
  }

  private void prepareStockPriceMap(String stock1Name, String stock2Name) {
    Map<String, Double> datePriceMap = new HashMap<>();
    datePriceMap.put(date, 45.0);
    datePriceMap.put(date2, 55.0);
    datePriceMap.put(date3, 65.0);
    stockPriceMap = new HashMap<>();
    stockPriceMap.put(stock1Name + "_DAILY", datePriceMap);
    stockPriceMap.put(stock2Name + "_DAILY", datePriceMap);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingPortfolioWithNullName() {
    user.createPortfolio(stockMap, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionCreatingPortfolioWithNoStocks() {
    user.createPortfolio(new HashMap<>(), portfolio1Name);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingPortfolioWithNullValues() {
    user.createPortfolio(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionCreatingPortfolioWithSameName() {
    user.createPortfolio(stockMap, portfolio1Name);
    stockMap.put("Stock2", 5.0);
    user.createPortfolio(stockMap, portfolio1Name);
  }

  @Test
  public void shouldCreatePortfolio() {
    user.createPortfolio(stockMap, portfolio1Name);
    List<String> expectedPortfolios = new ArrayList<>(List.of(portfolio1Name));
    List<String> actualPortfolios = user.listAllInflexiblePortfolioNames();
    for (int i = 0; i < actualPortfolios.size(); i++) {
      assertEquals(expectedPortfolios.get(i), actualPortfolios.get(i));
    }
  }

  @Test
  public void shouldCreateMultiplePortfolios() {
    user.createPortfolio(stockMap, portfolio1Name);
    stockMap.put("Stock2", 2.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    stockMap.put("Stock3", 4.0);
    user.createPortfolio(stockMap, "Portfolio 3");
    Set<String> expectedPortfolios = new HashSet<>(
        List.of(portfolio1Name, "Portfolio 2", "Portfolio 3"));
    List<String> actualPortfolios = user.listAllInflexiblePortfolioNames();

    assertEquals(expectedPortfolios.size(), actualPortfolios.size());
    for (String actualPortfolio : actualPortfolios) {
      assertTrue(expectedPortfolios.contains(actualPortfolio));
    }
  }

  @Test
  public void viewCompositionOfPortfolio() {
    for (int i = 0; i < 300; i++) {
      user.createPortfolio(stockMap, "Portfolio " + (i + 1));
      Map<String, Double> actualStocks = user.getCompositionOfPortfolio(
          "Portfolio " + (i + 1), null);

      Map<String, Double> expectedStocks = user.getCompositionOfPortfolio("Portfolio " + (i + 1),
          null);
      Set<String> expectedStocksSet = expectedStocks.keySet();

      assertEquals(expectedStocks.size(), actualStocks.size());
      for (Map.Entry<String, Double> actualStock : actualStocks.entrySet()) {
        assertTrue(expectedStocksSet.contains(actualStock.getKey()));
      }
      stocks.add(new MockStock("Stock" + (i + 2)));
      stockMap.put("Stock" + (i + 2), (double) i + 1);
    }
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetCompositionNullPortfolioNameGiven() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getCompositionOfPortfolio(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenGetCompositionWrongPortfolioNameGiven() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getCompositionOfPortfolio("Portfolio 4", null);
  }

  @Test
  public void emptyListIfNoPortfolio() {
    assertEquals(new ArrayList<>(), user.listAllInflexiblePortfolioNames());
    assertEquals(new ArrayList<>(), user.listAllFlexiblePortfolioNames());
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOfPortfolioWithNullPortfolioName() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getValuesOfStocksOfPortfolio(null, stockPriceMap, date);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOfPortfolioWithNullStockValues() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getValuesOfStocksOfPortfolio(portfolio1Name, null, date);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOfPortfolioWithNullDate() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getValuesOfStocksOfPortfolio(portfolio1Name, stockPriceMap, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenGetValueOfPortfolioWithNullValues() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getValuesOfStocksOfPortfolio(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenGetValueOfNonExistentPortfolio() {
    user.createPortfolio(stockMap, portfolio1Name);
    user.getValuesOfStocksOfPortfolio("Portfolio 4", stockPriceMap, date);
  }

  @Test
  public void shouldCheckIfPortfolioExistsOrNot() {
    user.createPortfolio(stockMap, portfolio1Name);

    assertTrue(user.inflexiblePortfolioExists(portfolio1Name));
    assertFalse(user.flexiblePortfolioExists(portfolio1Name));
  }

  @Test
  public void shouldCheckIfPortfolioDoesNotExist() {
    assertFalse(user.inflexiblePortfolioExists("Portfolio 2"));
    assertFalse(user.flexiblePortfolioExists("Portfolio 2"));
  }

  @Test
  public void getValueOfPortfolio() {
    user.createPortfolio(stockMap, portfolio1Name);
    Stock mockStock2 = new MockStock("Stock2");
    stocks.add(mockStock2);
    stockMap.put("Stock2", 100.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    Double valueOfPortfolio1 =
        user.getValuesOfStocksOfPortfolio("Portfolio 1", stockPriceMap, date);
    Double valueOfPortfolio2 =
        user.getValuesOfStocksOfPortfolio("Portfolio 2", stockPriceMap, date);

    assertEquals(90.0, valueOfPortfolio1, 0.0);
    assertEquals(4590.0, valueOfPortfolio2, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueOfPortfolioWithStockNotPresentInStockPriceMap() {
    user.createPortfolio(stockMap, portfolio1Name);
    Stock mockStock2 = new MockStock("Stock2");
    stocks.add(mockStock2);
    stockMap.put("Stock2", 100.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    stockPriceMap.remove("Stock2_DAILY");
    Double valueOfPortfolio = user.getValuesOfStocksOfPortfolio("Portfolio 2", stockPriceMap, date);
  }

  @Test
  public void getValueOfPortfolioWithStockPriceNotPresentForDate() {
    user.createPortfolio(stockMap, portfolio1Name);
    Stock mockStock2 = new MockStock("Stock2");
    stocks.add(mockStock2);
    stockMap.put("Stock2", 100.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    Double valueOfPortfolio = user.getValuesOfStocksOfPortfolio("Portfolio 2", stockPriceMap,
        FORMATTER.format(new Date()));

    assertEquals(6630.0, valueOfPortfolio, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueOfPortfolioWithStockPriceNotPresentInLast100Days() throws ParseException {
    user.createPortfolio(stockMap, portfolio1Name);
    Stock mockStock2 = new MockStock("Stock2");
    stocks.add(mockStock2);
    stockMap.put("Stock2", 100.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    Double valueOfPortfolio = user.getValuesOfStocksOfPortfolio("Portfolio 2", stockPriceMap,
        "2009-10-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueOfPortfolioWithNonExistentPortfolio() {
    user.createPortfolio(stockMap, portfolio1Name);
    Stock mockStock2 = new MockStock("Stock2");
    stocks.add(mockStock2);
    stockMap.put("Stock2", 100.0);
    user.createPortfolio(stockMap, "Portfolio 2");
    Double valueOfPortfolio = user.getValuesOfStocksOfPortfolio("Portfolio 3", stockPriceMap,
        FORMATTER.format(new Date()));
  }

  @Test(expected = NullPointerException.class)
  public void getValueOfPortfolioWithNullPortfolioName() {
    Double valueOfPortfolio =
        user.getValuesOfStocksOfPortfolio(null, stockPriceMap, date);
  }

  @Test(expected = NullPointerException.class)
  public void getValueOfPortfolioWithNullStockValues() {
    Double valueOfPortfolio =
        user.getValuesOfStocksOfPortfolio(portfolio1Name, null, date);
  }

  @Test(expected = NullPointerException.class)
  public void getValueOfPortfolioWithNullDate() {
    Double valueOfPortfolio =
        user.getValuesOfStocksOfPortfolio(portfolio1Name, stockPriceMap, null);
  }

  @Test(expected = NullPointerException.class)
  public void getValueOfPortfolioWithNullValues() {
    Double valueOfPortfolio =
        user.getValuesOfStocksOfPortfolio(null, null, null);
  }

  @Test
  public void loadExistingPortfolios() {
    StringBuilder log = new StringBuilder();
    FileUtil mockLog = new MockFileUtil(log);
    Map<String, List<String[]>> existingPortfolios = user.loadExistingPortfolio(mockLog,
        "filePath");

    assertEquals("filePath", log.toString());
  }

  @Test
  public void testSetCommission() {
    user.createPortfolio(null, portfolio1Name);
    user.setCommissionCharge(20.0);
    user.addTradeToFlexiblePortfolio(portfolio1Name, "Stock1", 1.0, "2022-10-10");

    assertTrue(user.flexiblePortfolioExists(portfolio1Name));
    assertEquals(65,
        user.getCostBasisForFlexiblePortfolio(portfolio1Name, "2022-10-10", stockPriceMap),
        0.0);
  }

  @Test
  public void testSavePortfolio() {
    user.createPortfolio(null, portfolio1Name);
    user.addTradeToFlexiblePortfolio(portfolio1Name, "Stock1", 1.0, "2022-10-10");
    StringBuilder log = new StringBuilder();
    FileUtil mockLog = new MockFileUtil(log);
    user.savePortfolio(portfolio1Name, "filePath", mockLog);

    String expected = "Portfolio 1\n"
        + "filePath\n"
        + "Stock Name,Quantity,Date,Type of Trade,\n"
        + "Stock1,1.0,2022-10-10,BUY,\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testCreateFlexiblePortfolio() {
    user.createPortfolio(null, portfolio1Name);

    assertTrue(user.flexiblePortfolioExists(portfolio1Name));
    assertEquals(0,
        user.getValuesOfStocksOfPortfolio(portfolio1Name, stockPriceMap, "2022-10-10"),
        0.0);
    assertEquals(0,
        user.getCostBasisForFlexiblePortfolio(portfolio1Name, "2022-10-10", stockPriceMap),
        0.0);
  }

  @Test
  public void testAddTrade() {
    user.createPortfolio(null, portfolio1Name);
    user.addTradeToFlexiblePortfolio(portfolio1Name, "Stock1", 1.0, "2022-10-10");

    assertTrue(user.flexiblePortfolioExists(portfolio1Name));
    assertEquals(45,
        user.getValuesOfStocksOfPortfolio(portfolio1Name, stockPriceMap, "2022-10-10"),
        0.0);
    assertEquals(55,
        user.getCostBasisForFlexiblePortfolio(portfolio1Name, "2022-10-10", stockPriceMap),
        0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddTradeWithNonExistentPortfolio() {
    user.addTradeToFlexiblePortfolio("Portfolio 2", "Stock1", 1.0, "2022-10-10");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisWithNonExistentPortfolio() {
    user.getCostBasisForFlexiblePortfolio("Portfolio 2", "2022-10-10", stockPriceMap);
  }

  @Test
  public void getPerformanceOfFlexiblePortfolio() {
    user.createPortfolio(null, portfolio1Name);
    user.addTradeToFlexiblePortfolio(portfolio1Name, stock1Name, 2.0, "2022-10-10");
    user.addTradeToFlexiblePortfolio(portfolio1Name, stock2Name, 3.0, "2022-10-11");
    user.addTradeToFlexiblePortfolio(portfolio1Name, stock1Name, 4.0, "2022-10-12");
    StringBuilder log = new StringBuilder();
    BiConsumer<Map<String, Double>, ApiPeriod> mockLog = new MockPerformanceScalerCommand(log);
    user.getPerformanceOfFlexiblePortfolio(portfolio1Name, "2022-10-10", "2022-10-12",
        stockPriceMap, mockLog);
    String expected = "DAILY\n"
        + "2022-10-10,90.0\n"
        + "2022-10-11,275.0\n"
        + "2022-10-12,585.0\n";
    assertEquals(expected, log.toString());
  }

  static class MockStock implements Stock {

    private final String name;

    public MockStock(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public Double getQuantity() {
      return 1.0;
    }
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
      log.append(filePath);
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