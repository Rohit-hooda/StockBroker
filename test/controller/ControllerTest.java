package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import model.User;
import model.UserImpl;
import org.junit.Before;
import org.junit.Test;
import util.ApiPeriod;
import util.FileUtil;
import view.StockAppView;
import view.StockAppViewImpl;

/**
 * Represents a Test class to test if behaviour of controller of the stocks application is correct.
 */
public class ControllerTest {

  private static final String RESOURCES_DIRECTORY = "test/resources/";
  private static final String STOCK_PRICE_DATA_DIRECTORY = "stock_value_data/";
  private static final String API_KEY = "dummy";

  private SampleInput sampleInput;
  private SampleOutput sampleOutput;

  @Before
  public void setUp() {
    sampleInput = new SampleInput();
    sampleOutput = new SampleOutput();
  }

  @Test
  public void getCompositionOfInflexiblePortfolioTest() {
    String input = sampleInput.getInputForCompositionOfInflexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedPortfolioName = inputLines[4].trim();
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String[] outputLines = log.toString().split("\n");

    assertEquals(expectedPortfolioName, outputLines[outputLines.length - 1]);
  }

  @Test
  public void getCompositionOfFlexiblePortfolioTest() {
    String input = sampleInput.getInputForCompositionOfFlexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedPortfolioName = inputLines[4].trim();
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String[] outputLines = log.toString().split("\n");

    assertEquals(expectedPortfolioName, outputLines[outputLines.length - 1]);
  }

  @Test
  public void getValueOfInflexiblePortfolioTest() {
    String input = sampleInput.getInputForValueOfInflexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedPortfolioName = inputLines[2].trim();
    String expectedDate = inputLines[3].trim();
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String expected = expectedPortfolioName + "," + expectedDate + "\n"
        + "AAPL_DAILY:\n"
        + expectedDate + ",155.74\n"
        + "\n"
        + "GOOG_DAILY:\n"
        + expectedDate + ",96.58\n"
        + "\n"
        + "AMZN_DAILY:\n"
        + expectedDate + ",103.41\n";

    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void getValueOfFlexiblePortfolioTest() {
    String input = sampleInput.getInputForValueOfFlexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedPortfolioName = inputLines[3].trim();
    String expectedDate = inputLines[4].trim();
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String expected = expectedPortfolioName + "," + expectedDate + "\n"
        + "AAPL_DAILY:\n"
        + expectedDate + ",155.74\n"
        + "\n"
        + "GOOG_DAILY:\n"
        + expectedDate + ",96.58\n"
        + "\n"
        + "AMZN_DAILY:\n"
        + expectedDate + ",103.41\n";

    assertTrue(log.toString().contains(expected));
  }

  @Test
  public void createInflexiblePortfolioManuallyTest() {
    String input = sampleInput.getInputForCreateInflexiblePortfolioManually();
    String[] inputLines = input.split("\n");
    String portfolioName = inputLines[5];
    String stock1Ticker = inputLines[7];
    String stock1Qty = String.valueOf(Double.parseDouble(inputLines[11]));
    String stock2Ticker = inputLines[13];
    String stock2Qty = String.valueOf(Double.parseDouble(inputLines[14]));
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String[] actualLines = log.toString().split("\n");

    assertEquals(portfolioName, actualLines[4].trim());
    assertEquals(stock1Ticker + "," + stock1Qty, actualLines[6].trim());
    assertEquals(stock2Ticker + "," + stock2Qty, actualLines[5].trim());

    deleteTempFiles(
        new String[]{RESOURCES_DIRECTORY + "inflexible_portfolios/" + portfolioName + ".csv",
            "res/stock_value_data/AAPLEE_DAILY_DAILY.csv"});
  }

  @Test
  public void createFlexiblePortfolioTest() {
    String input = sampleInput.getInputForCreateFlexiblePortfolio();
    String[] inputLines = input.split("\n");
    String portfolioName = inputLines[3];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String[] actualLines = log.toString().split("\n");

    assertEquals(portfolioName, actualLines[4].trim());

    deleteTempFiles(
        new String[]{RESOURCES_DIRECTORY + "inflexible_portfolios/" + portfolioName + ".csv"});
  }

  @Test
  public void addTradeToFlexibleTest() {
    String input = sampleInput.getInputForCreateFlexiblePortfolioAndAddTrade();
    String[] inputLines = input.split("\n");
    String portfolioName = inputLines[2];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String[] actualLines = log.toString().split("\n");

    assertEquals(portfolioName, actualLines[3].trim());

    deleteTempFiles(new String[]{"res/stock_value_data/AAPLEEEE_DAILY_DAILY.csv"});
  }

  @Test
  public void createInflexiblePortfolioByFileTest() throws IOException {
    String input = sampleInput.getInputForCreateInflexiblePortfolioByFile();
    String inputCsv = Files.readString(
        Paths.get(RESOURCES_DIRECTORY + "/TestPortfolio.csv"));
    String[] inputLines = inputCsv.split("\n");
    String portfolioName = "TestPortfolio";
    String[] line1 = inputLines[1].split(",");
    String stock1Ticker = line1[0];
    String stock1Qty = line1[1];
    String[] line2 = inputLines[2].split(",");
    String stock2Ticker = line2[0];
    String stock2Qty = line2[1];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String[] actualLines = log.toString().split("\n");

    assertEquals(portfolioName, actualLines[4].trim());
    assertEquals(stock1Ticker + "," + stock1Qty, actualLines[6].trim());
    assertEquals(stock2Ticker + "," + stock2Qty, actualLines[5].trim());
  }

  @Test
  public void testSetCommission() {
    String input = sampleInput.getInputForSetCommission();
    String[] inputLines = input.split("\n");
    Double expectedCommission = Double.parseDouble(inputLines[3]);
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String[] actualLines = log.toString().split("\n");

    assertEquals(expectedCommission, Double.parseDouble(actualLines[4].trim()), 0.0);
  }

  @Test
  public void testGetCostBasis() {
    String input = sampleInput.getInputForCostBasisOfFlexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedlog = inputLines[2] + "," + inputLines[3];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String[] actualLines = log.toString().split("\n");

    assertEquals(expectedlog, actualLines[4].trim());
  }

  @Test
  public void testGetPerformanceOfFlexiblePortfolio() {
    String input = sampleInput.getInputForPerformanceOfFlexiblePortfolio();
    String[] inputLines = input.split("\n");
    String expectedlog = inputLines[2] + "," + inputLines[3] + "," + inputLines[4];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    StringBuilder log = new StringBuilder();
    User mockLog = new MockUser(log);
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(mockLog, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();
    String[] actualLines = log.toString().split("\n");

    assertEquals(expectedlog, actualLines[4].trim());
  }

  @Test
  public void testPerformanceScalerForDaily() {
    OutputStream outputStream = new ByteArrayOutputStream();
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    BiConsumer<Map<String, Double>, ApiPeriod> scaler = new PerformanceScalerCommand(view);
    Map<String, Double> dateValueMap = new HashMap<>();
    dateValueMap.put("2022-10-10", 100.0);
    dateValueMap.put("2022-10-11", 200.0);
    dateValueMap.put("2022-10-13", 300.0);
    scaler.accept(dateValueMap, ApiPeriod.DAILY);
    String expected = "\n2022-10-10 : **********\n"
        + "2022-10-11 : ********************\n"
        + "2022-10-13 : ******************************\n"
        + "Scale of values : * = $10.0\n"
        + "Scale of timestamps : DAILY\n";

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void testPerformanceScalerForMonthly() {
    OutputStream outputStream = new ByteArrayOutputStream();
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    BiConsumer<Map<String, Double>, ApiPeriod> scaler = new PerformanceScalerCommand(view);
    Map<String, Double> dateValueMap = new HashMap<>();
    dateValueMap.put("2022-09-10", 100.0);
    dateValueMap.put("2022-10-10", 200.0);
    dateValueMap.put("2022-11-10", 300.0);
    scaler.accept(dateValueMap, ApiPeriod.MONTHLY);
    String expected = "\nSEP 2022 : **********\n"
        + "OCT 2022 : ********************\n"
        + "NOV 2022 : ******************************\n"
        + "Scale of values : * = $10.0\n"
        + "Scale of timestamps : MONTHLY\n";

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void goIntegrationTestHappyFlow() {
    String input = sampleInput.getInputForHappyFlowInflexiblePortfolio();
    String[] inputLines = input.split("\n");
    String portfolioName = inputLines[3];
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    User user = new UserImpl();
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(user, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String expectedOutput = sampleOutput.getOutputForHappyFlowInflexiblePortfolio();

    assertEquals(expectedOutput, outputStream.toString());

    deleteTempFiles(
        new String[]{RESOURCES_DIRECTORY + "inflexible_portfolios/" + portfolioName + ".csv"});
  }

  @Test
  public void goEndToEndIntegrationTestForInflexiblePortfolio() {
    String input = sampleInput.getInputForE2EInflexiblePortfolio();
    String portfolioName = "TestPortfolio";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    User user = new UserImpl();
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(user, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String expectedOutput = sampleOutput.getOutputForE2EInflexiblePortfolio();

    assertEquals(expectedOutput, outputStream.toString());

    deleteTempFiles(
        new String[]{RESOURCES_DIRECTORY + "inflexible_portfolios/" + portfolioName + ".csv",
            RESOURCES_DIRECTORY + "inflexible_portfolios/" + "endToEndPortfolio.csv",
            "res/stock_value_data/_DAILY_DAILY.csv"});
  }

  @Test
  public void goEndToEndIntegrationTestForFlexiblePortfolio() {
    String input = sampleInput.getInputForE2EFlexiblePortfolio();
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    User user = new UserImpl();
    StockAppView view = new StockAppViewImpl(new PrintStream(outputStream));
    StockAppController controller = new StockAppControllerImpl(user, view, inputStream,
        RESOURCES_DIRECTORY, STOCK_PRICE_DATA_DIRECTORY, API_KEY);
    controller.run();

    String expectedOutput = sampleOutput.getOutputForE2EFlexiblePortfolio();

    assertEquals(expectedOutput, outputStream.toString());

    deleteTempFiles(
        new String[]{RESOURCES_DIRECTORY + "flexible_portfolios/FlexPortfolio2.csv"});
  }

  private void deleteTempFiles(String[] filenames) {
    File file;
    for (String fileName : filenames) {
      file = new File(fileName);
      file.delete();
    }
  }

  static class MockUser implements User {

    private final StringBuilder sb;

    public MockUser(StringBuilder sb) {
      this.sb = sb;
    }

    @Override
    public void createPortfolio(Map<String, Double> stocks, String portfolioName)
        throws IllegalArgumentException {
      sb.append(portfolioName);
      sb.append("\n");
      if (stocks == null) {
        return;
      }
      for (Map.Entry<String, Double> stock : stocks.entrySet()) {
        sb.append(stock.getKey());
        sb.append(",");
        sb.append(stock.getValue());
        sb.append("\n");
      }
    }

    @Override
    public void addTradeToFlexiblePortfolio(String portfolioName, String ticker, Double quantity,
        String date) {
      // do nothing as it is a mock
    }

    @Override
    public void addFractionalTradeToFlexiblePortfolio(String portfolioName,
        Map<String, Map<String, Double>> stockPriceMap, Double amount,
        Map<String, Double> tickerRatios, String date) {
      // do nothing
    }

    @Override
    public Double getCommissionCharge() {
      return null;
    }

    @Override
    public void setCommissionCharge(Double commission) {
      sb.append(commission);
    }

    @Override
    public Double getCostBasisForFlexiblePortfolio(String portfolioName, String date,
        Map<String, Map<String, Double>> stockPriceMap) {
      sb.append(portfolioName);
      sb.append(",");
      sb.append(date);
      sb.append("\n");
      return null;
    }

    @Override
    public Map<String, Double> getCompositionOfPortfolio(String portfolioName, String date)
        throws IllegalArgumentException {
      sb.append(portfolioName);
      Map<String, Double> stockMap = new HashMap<>();
      stockMap.put("AAPL", 13.0);
      stockMap.put("GOOG", 12.0);
      return stockMap;
    }

    @Override
    public List<String> listAllInflexiblePortfolioNames() {
      return List.of("TestPortfolio2");
    }

    @Override
    public Double getValuesOfStocksOfPortfolio(String portfolioName,
        Map<String, Map<String, Double>> stockValues, String date)
        throws IllegalArgumentException, NullPointerException {
      sb.append(portfolioName);
      sb.append(",");
      sb.append(date);
      sb.append("\n");
      for (Map.Entry<String, Map<String, Double>> stock : stockValues.entrySet()) {
        sb.append(stock.getKey());
        sb.append(":");
        sb.append("\n");
        for (Map.Entry<String, Double> stockPriceOnDate : stock.getValue().entrySet()) {
          if (date.equals(stockPriceOnDate.getKey())) {
            sb.append(stockPriceOnDate.getKey());
            sb.append(",");
            sb.append(stockPriceOnDate.getValue());
            sb.append("\n");
          }
        }
        sb.append("\n");
      }
      return 0.0;
    }

    @Override
    public List<String> listAllFlexiblePortfolioNames() {
      return List.of("FlexPortfolio");
    }

    @Override
    public boolean inflexiblePortfolioExists(String portfolioName) {
      return Objects.equals("TestPortfolio2", portfolioName);
    }

    @Override
    public boolean flexiblePortfolioExists(String portfolioName) throws NullPointerException {
      return Objects.equals("FlexPortfolio", portfolioName);
    }

    @Override
    public void savePortfolio(String portfolioName, String filePath, FileUtil fileUtil) {
      // do nothing as it is a mock
    }

    @Override
    public Map<String, List<String[]>> loadExistingPortfolio(FileUtil fileUtil, String filePath) {
      Map<String, List<String[]>> existingPortfolios = new HashMap<>();
      if (filePath.contains("/flexible")) {
        List<String[]> portfolioData = new ArrayList<>();
        portfolioData.add(new String[]{"AAPL", "22.0", "2022-10-18", "BUY"});
        portfolioData.add(new String[]{"AAPL", "22.0", "2022-10-25", "SELL"});
        portfolioData.add(new String[]{"AAPL", "10.0", "2022-10-26", "BUY"});
        existingPortfolios.put("FlexPortfolio", portfolioData);
      } else {
        List<String[]> portfolioData = new ArrayList<>();
        portfolioData.add(new String[]{"AAPL", "13.0"});
        portfolioData.add(new String[]{"GOOG", "12.0"});
        existingPortfolios.put("TestPortfolio2", portfolioData);
      }

      return existingPortfolios;
    }

    @Override
    public void getPerformanceOfFlexiblePortfolio(String portfolioName, String from, String to,
        Map<String, Map<String, Double>> stockValues,
        BiConsumer<Map<String, Double>, ApiPeriod> performanceScaler)
        throws IllegalArgumentException {
      sb.append(portfolioName);
      sb.append(",");
      sb.append(from);
      sb.append(",");
      sb.append(to);
      sb.append("\n");
    }

    @Override
    public void addStrategyToFlexiblePortfolio(String portfolioName,
        Map<String, Map<String, Double>> stockPriceMap, Double amount,
        Map<String, Double> tickerRatios, String from, String to, ApiPeriod period)
        throws IllegalArgumentException {
      // do nothing
    }
  }

  /**
   * Represents an inner class that has methods that prepare various inputs for testing the
   * controller.
   */
  static class SampleInput {

    String getInputForCompositionOfInflexiblePortfolio() {
      return "1\n"
          + "2\n"
          + "\n"
          + "Wrong\n"
          + "TestPortfolio2\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForCompositionOfFlexiblePortfolio() {
      return "2\n"
          + "7\n"
          + "\n"
          + "Wrong\n"
          + "FlexPortfolio\n"
          + "2022-20-26\n"
          + "2022/10-26\n"
          + "\n"
          + "2022-10-26\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForValueOfInflexiblePortfolio() {
      return "1\n"
          + "3\n"
          + "TestPortfolio2\n"
          + "2022-10-28\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForValueOfFlexiblePortfolio() {
      return "2\n"
          + "5\n"
          + "FlexPortfolio2\n"
          + "FlexPortfolio\n"
          + "2022-10-28\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForCreateInflexiblePortfolioManually() {
      return "1\n"
          + "1\n"
          + "3\n"
          + "1\n"
          + "\n"
          + "TestPortfolio\n"
          + "AAPLEE\n"
          + "AAPL\n"
          + "-13\n"
          + "1.3\n"
          + "\n"
          + "13\n"
          + "y\n"
          + "GOOG\n"
          + "12\n"
          + "n\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForCreateInflexiblePortfolioByFile() {
      return "1\n"
          + "1\n"
          + "2\n"
          + "resources/TestPortfolio.csv\n"
          + "\n"
          + "test/resources/TestPortfolio.csv\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForCreateFlexiblePortfolio() {
      return "2\n"
          + "1\n"
          + "\n"
          + "TestPortfolio\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForCreateFlexiblePortfolioAndAddTrade() {
      return "2\n"
          + "2\n"
          + "FlexPortfolio\n"
          + "AAPLEEEE\n"
          + "\n"
          + "AAPL\n"
          + "32\n"
          + "2022-10-10\n"
          + "3\n"
          + "FlexPortfolio\n"
          + "AAPL\n"
          + "2\n"
          + "2022-90-12\n"
          + "2023-10-12\n"
          + "2022-10-12\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForSetCommission() {
      return "3\n"
          + "-20\n"
          + "\n"
          + "20\n"
          + "exit\n";
    }

    String getInputForCostBasisOfFlexiblePortfolio() {
      return "2\n"
          + "4\n"
          + "FlexPortfolio\n"
          + "2022-10-25\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForPerformanceOfFlexiblePortfolio() {
      return "2\n"
          + "6\n"
          + "FlexPortfolio\n"
          + "2022-10-18\n"
          + "2022-10-26\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForHappyFlowInflexiblePortfolio() {
      return "1\n"
          + "1\n"
          + "1\n"
          + "TestPortfolio2\n"
          + "AAPL\n"
          + "13\n"
          + "y\n"
          + "GOOG\n"
          + "12\n"
          + "n\n"
          + "2\n"
          + "TestPortfolio2\n"
          + "3\n"
          + "TestPortfolio2\n"
          + "2022-10-28\n"
          + "back\n"
          + "exit\n";
    }


    String getInputForE2EInflexiblePortfolio() {
      return "\n"
          + "popo\n"
          + "1\n"
          + "\n"
          + "popo\n"
          + "0\n"
          + "1\n"
          + "\n"
          + "1\n"
          + "endToEndPortfolio\n"
          + "\n"
          + "AAPL\n"
          + "\n"
          + "popo\n"
          + "10.19\n"
          + "-90\n"
          + "90\n"
          + "\n"
          + "abc\n"
          + "y\n"
          + "GOOG\n"
          + "23\n"
          + "n\n"
          + "1\n"
          + "2\n"
          + "invalidpath\n"
          + "\n"
          + "test/resources/TestPortfolio.csv\n"
          + "2\n"
          + "\n"
          + "noname\n"
          + "endToEndPortfolio\n"
          + "2\n"
          + "TestPortfolio\n"
          + "3\n"
          + "\n"
          + "noname\n"
          + "endToEndPortfolio\n"
          + "\n"
          + "2025-10-10\n"
          + "999999\n"
          + "2022-10-28\n"
          + "3\n"
          + "TestPortfolio\n"
          + "2022-10-28\n"
          + "back\n"
          + "exit\n";
    }

    String getInputForE2EFlexiblePortfolio() {
      return "2\n"
          + "1\n"
          + "FlexPortfolio2\n"
          + "2\n"
          + "FlexPortfolio2\n"
          + "AAPL\n"
          + "10\n"
          + "2022-08-08\n"
          + "2\n"
          + "FlexPortfolio2\n"
          + "GOOG\n"
          + "20\n"
          + "2022-09-12\n"
          + "3\n"
          + "FlexPortfolio2\n"
          + "AAPL\n"
          + "5\n"
          + "2022-11-11\n"
          + "4\n"
          + "FlexPortfolio2\n"
          + "2022-11-12\n"
          + "5\n"
          + "FlexPortfolio2\n"
          + "2022-11-12\n"
          + "6\n"
          + "FlexPortfolio2\n"
          + "2022-07-25\n"
          + "2022-11-15\n"
          + "7\n"
          + "FlexPortfolio2\n"
          + "2022-09-13\n"
          + "back\n"
          + "exit\n";
    }
  }

  /**
   * Represents an inner class that has methods that contain various outputs for testing the
   * controller.
   */
  static class SampleOutput {

    String getOutputForHappyFlowInflexiblePortfolio() {
      return "***Stocks App Starting***\n"
          + "Loading initial data...\n"
          + "Application is now ready to use!\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below ways to create a Portfolio of stocks:\n"
          + "Type '1' to create a new Portfolio by ticker and quantity\n"
          + "Type '2' to create a new Portfolio by a csv file\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of GOOG:\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "TestPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Details for TestPortfolio2 Portfolio\n"
          + "Ticker : Quantity\n"
          + "GOOG : 12.0\n"
          + "AAPL : 13.0\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "TestPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Value of Portfolio 'TestPortfolio2' on 2022-10-28 is: 3183.58\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "****Stock App Closing****\n";
    }

    String getOutputForE2EInflexiblePortfolio() {
      return "***Stocks App Starting***\n"
          + "Loading initial data...\n"
          + "Application is now ready to use!\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "Could not recognize the input!\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "Could not recognize the input!\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Could not recognize the input!\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Could not recognize the input!\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Could not recognize the input!\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below ways to create a Portfolio of stocks:\n"
          + "Type '1' to create a new Portfolio by ticker and quantity\n"
          + "Type '2' to create a new Portfolio by a csv file\n"
          + "Could not recognize the input!\n"
          + "Choose from the below ways to create a Portfolio of stocks:\n"
          + "Type '1' to create a new Portfolio by ticker and quantity\n"
          + "Type '2' to create a new Portfolio by a csv file\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid ticker name\n"
          + "Ticker with name '' not supported by the application!\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Invalid quantity entered! Only Positive Whole Numbers are accepted\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Invalid quantity entered! Only Positive Whole Numbers are accepted\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Invalid quantity entered! Only Positive Whole Numbers are accepted\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Invalid quantity entered! Only Positive Whole Numbers are accepted\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of GOOG:\n"
          + "Choose from the below options to proceed:\n"
          + "Type 'y' to continue adding stocks to the portfolio\n"
          + "Type 'n' to finish creating portfolio and save it\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below ways to create a Portfolio of stocks:\n"
          + "Type '1' to create a new Portfolio by ticker and quantity\n"
          + "Type '2' to create a new Portfolio by a csv file\n"
          + "Enter a valid file path:\n"
          + "Invalid file path given! Please try again!\n"
          + "Enter a valid file path:\n"
          + "Invalid file path given! Please try again!\n"
          + "Enter a valid file path:\n"
          + "Portfolio created using file test/resources/TestPortfolio.csv!\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "endToEndPortfolio\n"
          + "TestPortfolio\n"
          + "Please type the name of Portfolio\n"
          + "Portfolio does not exist with name: ''\n"
          + "Please type the name of Portfolio\n"
          + "Portfolio does not exist with name: 'noname'\n"
          + "Please type the name of Portfolio\n"
          + "Details for endToEndPortfolio Portfolio\n"
          + "Ticker : Quantity\n"
          + "GOOG : 23.0\n"
          + "AAPL : 90.0\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "endToEndPortfolio\n"
          + "TestPortfolio\n"
          + "Please type the name of Portfolio\n"
          + "Details for TestPortfolio Portfolio\n"
          + "Ticker : Quantity\n"
          + "GOOG : 13.0\n"
          + "AAPL : 12.0\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "endToEndPortfolio\n"
          + "TestPortfolio\n"
          + "Please type the name of Portfolio\n"
          + "Portfolio does not exist with name: ''\n"
          + "Please type the name of Portfolio\n"
          + "Portfolio does not exist with name: 'noname'\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Could not recognize the input!\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Could not recognize the input!\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Could not recognize the input!\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Value of Portfolio 'endToEndPortfolio' on 2022-10-28 is: 16237.94\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "endToEndPortfolio\n"
          + "TestPortfolio\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Value of Portfolio 'TestPortfolio' on 2022-10-28 is: 3124.42\n"
          + "Choose from the below choices for Inflexible Portfolios:\n"
          + "Type '1' to create an Inflexible Portfolio\n"
          + "Type '2' to view composition of Inflexible Portfolio\n"
          + "Type '3' to view value of Inflexible Portfolio\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "****Stock App Closing****\n";
    }

    String getOutputForE2EFlexiblePortfolio() {
      return "***Stocks App Starting***\n"
          + "Loading initial data...\n"
          + "Application is now ready to use!\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "Please type the name of Portfolio\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "BUY performed successfully!\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of GOOG:\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "BUY performed successfully!\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid ticker name\n"
          + "Enter quantity of stocks of AAPL:\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "SELL performed successfully!\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Cost basis for FlexPortfolio2 on 2022-11-12 is 3916.1000000000004\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Value of Portfolio 'FlexPortfolio2' on 2022-11-12 is: 2683.1000000000004\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Enter date from which you want to see performance \n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Enter date upto which you want to see performance \n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Performance of portfolio 'FlexPortfolio2' from 2022-07-25 to 2022-11-15\n"
          + "\n"
          + "2022-07-29 : \n"
          + "2022-08-05 : \n"
          + "2022-08-12 : **************\n"
          + "2022-08-19 : **************\n"
          + "2022-08-26 : *************\n"
          + "2022-09-02 : *************\n"
          + "2022-09-09 : *************\n"
          + "2022-09-16 : ******************************\n"
          + "2022-09-23 : *****************************\n"
          + "2022-09-30 : ***************************\n"
          + "2022-10-07 : ****************************\n"
          + "2022-10-14 : ***************************\n"
          + "2022-10-21 : *****************************\n"
          + "2022-10-28 : *****************************\n"
          + "2022-11-04 : **************************\n"
          + "2022-11-11 : **********************\n"
          + "Scale of values : * = $119.32\n"
          + "Scale of timestamps : WEEKLY\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "These are the existing portfolios:\n"
          + "FlexPortfolio\n"
          + "FlexPortfolio2\n"
          + "Please type the name of Portfolio\n"
          + "Please enter date on which you want to see the "
          + "composition for Portfolio FlexPortfolio2\n"
          + "Enter a valid date in the format yyyy-MM-dd:\n"
          + "Details for FlexPortfolio2 Portfolio\n"
          + "Ticker : Quantity\n"
          + "GOOG : 20.0\n"
          + "AAPL : 10.0\n"
          + "Choose from the below choices for Flexible Portfolios:\n"
          + "Type '1' to create a Flexible Portfolio\n"
          + "Type '2' to buy a stock by quantity\n"
          + "Type '3' to sell a stock\n"
          + "Type '4' to view cost basis of a Flexible Portfolio\n"
          + "Type '5' to view value of a Flexible Portfolio\n"
          + "Type '6' to view performance of a Flexible Portfolio\n"
          + "Type '7' to view composition of a Flexible Portfolio\n"
          + "Type '8' to buy stocks by amount\n"
          + "Type '9' to add a strategy to invest\n"
          + "Type 'back' to go back to the main menu\n"
          + "Choose from the below choices for Portfolio of stocks:\n"
          + "Type '1' to work with Inflexible Portfolios\n"
          + "Type '2' to work with Flexible Portfolios\n"
          + "Type '3' to configure commission charge\n"
          + "Type 'exit' to exit the application\n"
          + "****Stock App Closing****\n";
    }
  }
}