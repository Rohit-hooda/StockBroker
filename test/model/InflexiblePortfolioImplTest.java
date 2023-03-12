package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import util.FileUtil;

/**
 * Represents a Test class to test if behaviour of {@link InflexiblePortfolioImpl} is correct.
 */
public class InflexiblePortfolioImplTest {

  private Portfolio portfolio;
  private List<Stock> stockList;

  @Before
  public void setUp() {
    Stock mockStock1 = new MockStock("Stock1");
    Stock mockStock2 = new MockStock("Stock2");
    stockList = new ArrayList<>();
    stockList.add(mockStock1);
    stockList.add(mockStock2);
    portfolio = new InflexiblePortfolioImpl("Portfolio 1", stockList);
  }

  @Test
  public void getPortfolioName() {
    assertEquals("Portfolio 1", portfolio.getPortfolioName());
  }

  @Test
  public void getComposition() {
    Map<String, Double> actualStocks = portfolio.getComposition("2022-11-10");

    for (int i = 0; i < actualStocks.size(); i++) {
      assertTrue(actualStocks.containsKey(stockList.get(i).getName()));
      assertEquals(stockList.get(i).getQuantity(), actualStocks.get("Stock" + (i + 1)));
    }
  }

  @Test
  public void getCompositionWithNullDate() {
    Map<String, Double> actualStocks = portfolio.getComposition(null);

    for (int i = 0; i < actualStocks.size(); i++) {
      assertTrue(actualStocks.containsKey(stockList.get(i).getName()));
      assertEquals(stockList.get(i).getQuantity(), actualStocks.get("Stock" + (i + 1)));
    }
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingPortfolioWithNullPortfolioName() {
    portfolio = new InflexiblePortfolioImpl(null, stockList);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingPortfolioWithNullStocks() {
    portfolio = new InflexiblePortfolioImpl("Portfolio 1", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionCreatingPortfolioWithZeroStocks() {
    portfolio = new InflexiblePortfolioImpl("Portfolio 1", new ArrayList<>());
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingPortfolioWithNullValues() {
    portfolio = new InflexiblePortfolioImpl(null, null);
  }

  @Test
  public void testSavePortfolio() {
    StringBuilder log = new StringBuilder();
    FileUtil mockFileUtil = new MockFileUtil(log);
    portfolio.savePortfolio("filePath", mockFileUtil);

    String expected = "Portfolio 1\n"
        + "filePath\n"
        + "Stock Name,Quantity,\n"
        + "Stock1,1.0,\n"
        + "Stock2,1.0,\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void checkEqualPortfolios() {
    Portfolio portfolioCopy = new InflexiblePortfolioImpl("Portfolio 1", stockList);

    assertNotSame(portfolio, portfolioCopy);
    assertEquals(portfolio.hashCode(), portfolioCopy.hashCode());
    assertEquals(portfolio, portfolioCopy);
  }

  @Test
  public void checkUnequalPortfolios() {
    assertNotEquals(null, portfolio);
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

  class MockStock implements Stock {

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
}