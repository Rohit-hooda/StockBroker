package view;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import util.ApiPeriod;

/**
 * Represents a Test class to test if behaviour of {@link StockAppViewImpl} is correct.
 */
public class StockAppViewImplTest {

  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
  private StockAppView view;
  private OutputStream outputStream;
  private String date;

  @Before
  public void setUp() throws Exception {
    outputStream = new ByteArrayOutputStream();
    view = new StockAppViewImpl(new PrintStream(outputStream));
    Date today = new Date();
    date = FORMATTER.format(today);
  }

  @Test
  public void showInitialDataIsLoading() {
    String expected = "Loading initial data...\n";
    view.showInitialDataIsLoading();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showUnsupportedTickerName() {
    String expected = "Ticker with name 'XYZ' not supported by the application!\n";
    view.showUnsupportedTickerName("XYZ");

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showUnsupportedTickerNameWithNullTicker() {
    view.showUnsupportedTickerName(null);
  }

  @Test
  public void showInvalidQuantityEntered() {
    String expected = "Invalid quantity entered! Only Positive Whole Numbers are accepted\n";
    view.showInvalidQuantityEntered();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPortfolioAlreadyExists() {
    String expected = "Portfolio with name 'duplicateportfolio' already exists!\n";
    view.showPortfolioAlreadyExists("duplicateportfolio");

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showPortfolioAlreadyExistsWithNullPortfolioName() {
    view.showPortfolioAlreadyExists(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void showPortfolioAlreadyExistsWithEmptyPortfolioName() {
    view.showPortfolioAlreadyExists("");
  }

  @Test
  public void showStockCreationUnsuccessful() {
    String expected = "Stock could not be created! Please try again!\n";
    view.showStockCreationUnsuccessful();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPortfolioCreationUnsuccessful() {
    String expected = "Portfolio could not be created! Please try again!\n";
    view.showPortfolioCreationUnsuccessful();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPortfolioDoesNotExist() {
    String expected = "Portfolio does not exist with name: 'newPortfolio'\n";
    view.showPortfolioDoesNotExist("newPortfolio");

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showPortfolioDoesNotExistWithNullPortfolioName() {
    view.showPortfolioDoesNotExist(null);
  }

  @Test
  public void askFilePathOfPortfolio() {
    String expected = "Enter a valid file path:\n";
    view.askFilePathOfPortfolio();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showFileNotPermitted() {
    String expected = "Application does not have permission to access wrongpath/\n";
    view.showFileNotPermitted("wrongpath/");

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showFileNotPermittedWithNullFilePath() {
    view.showFileNotPermitted(null);
  }

  @Test
  public void showLoadPortfolioByFileUnsuccessful() {
    String expected = "Portfolio could not be created using file testportfolio.csv! "
        + "Please try again!\n";
    view.showLoadPortfolioByFileUnsuccessful("testportfolio.csv");

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showLoadPortfolioByFileUnsuccessfulWithNullFilePath() {
    view.showLoadPortfolioByFileUnsuccessful(null);
  }

  @Test
  public void showLoadPortfolioByFileSuccessful() {
    String expected = "Portfolio created using file testportfolio.csv!\n";
    view.showLoadPortfolioByFileSuccessful("testportfolio.csv");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showInvalidFilePath() {
    String expected = "Invalid file path given! Please try again!\n";
    view.showInvalidFilePath();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askDate() {
    String expected = "Enter a valid date in the format yyyy-MM-dd:\n";
    view.askDate();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showStockDetailsNotAvailableForDate() {
    String expected =
        "Portfolio value for testportfolio on " + date + " is not available!\n";
    view.showPortfolioDetailsNotAvailableForDate("testportfolio", date);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showInitializationFailed() {
    String expected = "Application startup failed! testportfolio.csv could not be loaded!\n";
    view.showInitializationFailed("testportfolio.csv");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showAppStart() throws IOException {
    view.showAppStart();

    assertEquals("***Stocks App Starting***\n", outputStream.toString());
    outputStream.flush();
  }

  @Test
  public void showTickerSymbolsLoading() {
    view.showInitialDataIsLoading();

    assertEquals("Loading initial data...\n", outputStream.toString());
  }

  @Test
  public void askUserForPortfolioActions() {
    String expected = "Choose from the below choices for Portfolio of stocks:\n"
        + "Type '1' to work with Inflexible Portfolios\n"
        + "Type '2' to work with Flexible Portfolios\n"
        + "Type '3' to configure commission charge\n"
        + "Type 'exit' to exit the application\n";
    view.showMainMenu();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askNameOfPortfolio() {
    String expected = "Please type the name of Portfolio\n";
    view.askNameOfPortfolio();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showExitAppMessage() {
    String expected = "****Stock App Closing****\n";
    view.showExitAppMessage();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showEmptyPortfolioDetails() {
    String portfolioName = "First";
    String expected = "Portfolio does not exist with name: '" + portfolioName + "'\n";
    view.showPortfolioDetails(null, portfolioName, 0.0, date);

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showEmptyPortfolioDetailsWithNullPortfolioName() {
    List<String> stocks = new ArrayList<>();
    stocks.add("AAPL : 12.0");
    view.showPortfolioDetails(stocks, null, 0.0, date);
  }

  @Test(expected = NullPointerException.class)
  public void showEmptyPortfolioDetailsWithNullTotal() {
    List<String> stocks = new ArrayList<>();
    stocks.add("AAPL : 12.0");
    view.showPortfolioDetails(stocks, "testportfolio", null, date);
  }

  @Test(expected = NullPointerException.class)
  public void showEmptyPortfolioDetailsWithNullValues() {
    view.showPortfolioDetails(null, null, null, null);
  }

  @Test
  public void showPortfolioDetails() {
    String portfolioName = "dummy portfolio";
    List<String> stocks = new ArrayList<>();
    stocks.add("AAPL : 12.0");
    view.showPortfolioDetails(stocks, portfolioName, 12.0, null);

    assertEquals("Details for " + portfolioName + " Portfolio\n"
        + "AAPL : 12.0\nTotal Value of dummy portfolio is $12.0\n", outputStream.toString());
  }

  @Test
  public void showPortfolioDetailsOnDate() {
    String portfolioName = "dummy portfolio";
    List<String> stocks = new ArrayList<>();
    stocks.add("AAPL : 12.0");
    view.showPortfolioDetails(stocks, portfolioName, 0.0, date);

    assertEquals("Details for " + portfolioName + " Portfolio on " + date
            + "\nAAPL : 12.0\n",
        outputStream.toString());
  }

  @Test
  public void showAllPortfolioNames() {
    List<String> portfolioNames = new ArrayList<>();
    portfolioNames.add("dummy portfolio1");
    portfolioNames.add("dummy portfolio2");
    portfolioNames.add("dummy portfolio3");
    view.showAllPortfolioNames(portfolioNames);
    StringBuilder sb = new StringBuilder("These are the existing portfolios:\n");
    for (String str : portfolioNames) {
      sb.append(str);
      sb.append("\n");
    }

    assertEquals(sb.toString(), outputStream.toString());
  }

  @Test
  public void showAllPortfolioNamesWithEmptyList() {
    List<String> portfolioNames = new ArrayList<>();
    view.showAllPortfolioNames(portfolioNames);
    String expected = "No portfolios exist!\n";

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void showAllPortfolioNamesWithNullList() {
    view.showAllPortfolioNames(null);
  }

  @Test
  public void showInitializationComplete() {
    String expected = "Application is now ready to use!\n";
    view.showInitializationComplete();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showInvalidUserInput() {
    String expected = "Could not recognize the input!\n";
    view.showInvalidUserInput();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askWayToCreatePortfolio() {
    String expected = "Choose from the below ways to create a Portfolio of stocks:\n"
        + "Type '1' to create a new Portfolio by ticker and quantity\n"
        + "Type '2' to create a new Portfolio by a csv file\n";
    view.askWayToCreatePortfolio();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askTickerOfStock() {
    String expected = "Enter a valid ticker name\n";
    view.askTickerOfStock();

    assertEquals(expected, outputStream.toString());
  }

  @Test(expected = NullPointerException.class)
  public void askQuantityOfStockNullStock() {
    view.askQuantityOfStock(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void askQuantityOfStockEmptyStock() {
    view.askQuantityOfStock("");
  }

  @Test
  public void askQuantityOfStock() {
    String expected = "Enter quantity of stocks of dummyStock:\n";
    view.askQuantityOfStock("dummyStock");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askToSavePortfolio() {
    String expected = "Choose from the below options to proceed:\n" +
        "Type 'y' to continue adding stocks to the portfolio\n" +
        "Type 'n' to finish creating portfolio and save it\n";
    view.askToSavePortfolio();

    assertEquals(expected, outputStream.toString());
  }


  @Test
  public void showPortfolioNameCannotBeEmpty() {
    String expected = "Portfolio name cannot be blank!\n";
    view.showPortfolioNameCannotBeEmpty();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showStockDetailsNotAvailableOnDate() {
    String expected = "Stock data for AAPL on 2022-10-10 is not available!\n";
    view.showStockDetailsNotAvailableOnDate("AAPL", "2022-10-10");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPortfolioValue() {
    String expected = "Value of Portfolio 'Portfolio 1' on 2022-10-09 is: 300.0\n";
    view.showPortfolioValue("Portfolio 1", "2022-10-09", 300.0);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askCommissionChargeToSet() {
    String expected = "Please enter the amount of commission to configure:\n";
    view.askCommissionChargeToSet();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showInvalidNumberEntered() {
    String expected = "Invalid value entered for Stock Name\n";
    String input = "Stock Name";
    view.showInvalidNumberEntered(input);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showInflexiblePortfolioMenu() {
    String expected = "Choose from the below choices for Inflexible Portfolios:\n"
        + "Type '1' to create an Inflexible Portfolio\n"
        + "Type '2' to view composition of Inflexible Portfolio\n"
        + "Type '3' to view value of Inflexible Portfolio\n"
        + "Type 'back' to go back to the main menu\n";
    view.showInflexiblePortfolioMenu();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showFlexiblePortfolioMenu() {
    String expected = "Choose from the below choices for Flexible Portfolios:\n"
        + "Type '1' to create a Flexible Portfolio\n"
        + "Type '2' to buy a stock by quantity\n"
        + "Type '3' to sell a stock\n"
        + "Type '4' to view cost basis of a Flexible Portfolio\n"
        + "Type '5' to view value of a Flexible Portfolio\n"
        + "Type '6' to view performance of a Flexible Portfolio\n"
        + "Type '7' to view composition of a Flexible Portfolio\n"
        + "Type '8' to buy stocks by amount\n"
        + "Type '9' to add a strategy to invest\n"
        + "Type 'back' to go back to the main menu\n";
    view.showFlexiblePortfolioMenu();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showError() {
    String expected = "Invalid Quantity entered\n";
    view.showError("Invalid Quantity entered");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showCostBasisOfPortfolio() {
    String expected = "Cost basis for Portfolio 1 on 2022-02-10 is 200.0\n";
    view.showCostBasisOfPortfolio("Portfolio 1", 200.0, "2022-02-10");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askFromDate() {
    String expected = "Enter date from which you want to see performance \n";
    view.askFromDate();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askToDate() {
    String expected = "Enter date upto which you want to see performance \n";
    view.askToDate();

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPerformanceHeader() {
    String expected = "Performance of portfolio 'Portfolio 1' from 2022-02-10 to 2022-09-11\n";
    view.showPerformanceHeader("Portfolio 1", "2022-02-10", "2022-09-11");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPerformanceDaily() {
    Map<String, Integer> scaledPerformances = new HashMap<>();
    scaledPerformances.put("2022-09-19", 10);
    scaledPerformances.put("2022-09-20", 15);
    scaledPerformances.put("2022-09-18", 11);
    ApiPeriod timeScale = ApiPeriod.DAILY;
    double valueScale = 100.0;

    String expected = "\n"
        + "2022-09-18 : ***********\n"
        + "2022-09-19 : **********\n"
        + "2022-09-20 : ***************\n"
        + "Scale of values : * = $100.0\n"
        + "Scale of timestamps : DAILY\n";
    view.showPerformance(scaledPerformances, timeScale, valueScale);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPerformanceWeekly() {
    Map<String, Integer> scaledPerformances = new HashMap<>();
    scaledPerformances.put("2022-09-19", 10);
    scaledPerformances.put("2022-09-10", 15);
    scaledPerformances.put("2022-08-18", 11);
    ApiPeriod timeScale = ApiPeriod.WEEKLY;
    double valueScale = 100.0;

    String expected = "\n2022-09-19 : **********\n"
        + "2022-08-18 : ***********\n"
        + "2022-09-10 : ***************\n"
        + "Scale of values : * = $100.0\n"
        + "Scale of timestamps : WEEKLY\n";
    view.showPerformance(scaledPerformances, timeScale, valueScale);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showPerformanceMonthly() {
    Map<String, Integer> scaledPerformances = new HashMap<>();
    scaledPerformances.put("2022-09-19", 10);
    scaledPerformances.put("2022-07-10", 15);
    scaledPerformances.put("2022-04-18", 11);
    ApiPeriod timeScale = ApiPeriod.MONTHLY;
    double valueScale = 100.0;

    String expected = "\nJUL 2022 : ***************\n"
        + "SEP 2022 : **********\n"
        + "APR 2022 : ***********\n"
        + "Scale of values : * = $100.0\n"
        + "Scale of timestamps : MONTHLY\n";
    view.showPerformance(scaledPerformances, timeScale, valueScale);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showTradeSuccessful() {
    String expected = "BUY performed successfully!\n";
    view.showTradeSuccessful("BUY");

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void showCommissionChanged() {
    String expected = "Commission fess changed to 20.0\n";
    view.showCommissionChanged(20.0);

    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void askDateForComposition() {
    String expected = "Please enter date on which you want to see the composition for Portfolio "
        + "Portfolio 1\n";
    view.askDateForComposition("Portfolio 1");

    assertEquals(expected, outputStream.toString());
  }
}