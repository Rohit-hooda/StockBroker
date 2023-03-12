package view;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import util.ApiPeriod;
import util.ParamNullChecker;

/**
 * Represents an implementation of {@link StockAppView} that enables to print messages from the
 * application to output stream provided.
 */
public final class StockAppViewImpl implements StockAppView {

  private final Consumer<Object[]> nullChecker;
  private final PrintStream out;

  /**
   * Constructs an instance of StockAppViewImpl that prints different messages on the output stream
   * that can be used to show to end user and keep the application interactive.
   *
   * @param out output stream to print the texts on
   */
  public StockAppViewImpl(PrintStream out) {
    nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{out});
    this.out = out;
  }

  private boolean isInvalidString(String string) {
    return string.isBlank() || string.isEmpty();
  }

  @Override
  public void showAppStart() {
    this.out.println("***Stocks App Starting***");
  }

  @Override
  public void showInitialDataIsLoading() {
    this.out.println("Loading initial data...");
  }

  @Override
  public void showMainMenu() {
    this.out.println("Choose from the below choices for Portfolio of stocks:");
    this.out.println("Type '1' to work with Inflexible Portfolios");
    this.out.println("Type '2' to work with Flexible Portfolios");
    this.out.println("Type '3' to configure commission charge");
    this.out.println("Type 'exit' to exit the application");
  }

  @Override
  public void askNameOfPortfolio() {
    this.out.println("Please type the name of Portfolio");
  }

  @Override
  public void showExitAppMessage() {
    this.out.println("****Stock App Closing****");
  }

  @Override
  public void showPortfolioDetails(List<String> stocksAsString, String portfolioName,
      Double total, String date) {
    nullChecker.accept(new Object[]{portfolioName, total});
    if (stocksAsString != null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Details for ").append(portfolioName).append(" Portfolio");
      if (date != null) {
        sb.append(" on ").append(date);
      }
      this.out.println(sb);
      for (String stockData : stocksAsString) {
        this.out.println(stockData);
      }
      if (!total.equals(0.0)) {
        this.out.println("Total Value of " + portfolioName + " is $" + total);
      }
    } else {
      this.out.println("Portfolio does not exist with name: '" + portfolioName + "'");
    }
  }

  @Override
  public void showAllPortfolioNames(List<String> portfolioNames) {
    nullChecker.accept(new Object[]{portfolioNames});
    if (portfolioNames.size() == 0) {
      this.out.println("No portfolios exist!");
    } else {
      this.out.println("These are the existing portfolios:");
      portfolioNames.forEach(this.out::println);
    }
  }

  @Override
  public void showInitializationComplete() {
    this.out.println("Application is now ready to use!");
  }

  @Override
  public void showInvalidUserInput() {
    this.out.println("Could not recognize the input!");
  }

  @Override
  public void askWayToCreatePortfolio() {
    this.out.println("Choose from the below ways to create a Portfolio of stocks:");
    this.out.println("Type '1' to create a new Portfolio by ticker and quantity");
    this.out.println("Type '2' to create a new Portfolio by a csv file");
  }

  @Override
  public void askTickerOfStock() {
    this.out.println("Enter a valid ticker name");
  }

  @Override
  public void askQuantityOfStock(String stockName) {
    nullChecker.accept(new Object[]{stockName});
    if (isInvalidString(stockName)) {
      throw new IllegalArgumentException("Invalid stock name");
    }
    this.out.println("Enter quantity of stocks of " + stockName + ":");
  }

  @Override
  public void askToSavePortfolio() {
    this.out.println("Choose from the below options to proceed:");
    this.out.println("Type 'y' to continue adding stocks to the portfolio");
    this.out.println("Type 'n' to finish creating portfolio and save it");
  }

  @Override
  public void showUnsupportedTickerName(String ticker) {
    nullChecker.accept(new Object[]{ticker});
    this.out.println("Ticker with name '" + ticker + "' not supported by the application!");
  }

  @Override
  public void showInvalidQuantityEntered() {
    this.out.println("Invalid quantity entered! Only Positive Whole Numbers are accepted");
  }

  @Override
  public void showPortfolioAlreadyExists(String portfolioName) {
    nullChecker.accept(new Object[]{portfolioName});
    if (isInvalidString(portfolioName)) {
      throw new IllegalArgumentException("Invalid stock name");
    }
    this.out.println("Portfolio with name '" + portfolioName + "' already exists!");
  }

  @Override
  public void showStockCreationUnsuccessful() {
    this.out.println("Stock could not be created! Please try again!");
  }

  @Override
  public void showPortfolioCreationUnsuccessful() {
    this.out.println("Portfolio could not be created! Please try again!");
  }

  @Override
  public void showPortfolioDoesNotExist(String portfolioName) {
    nullChecker.accept(new Object[]{portfolioName});
    this.out.println("Portfolio does not exist with name: '" + portfolioName + "'");
  }

  @Override
  public void askFilePathOfPortfolio() {
    this.out.println("Enter a valid file path:");
  }

  @Override
  public void showFileNotPermitted(String path) {
    nullChecker.accept(new Object[]{path});
    this.out.println("Application does not have permission to access " + path);
  }

  @Override
  public void showLoadPortfolioByFileUnsuccessful(String filePath) {
    nullChecker.accept(new Object[]{filePath});
    this.out.println(
        "Portfolio could not be created using file " + filePath + "! Please try again!");
  }

  @Override
  public void showLoadPortfolioByFileSuccessful(String filePath) {
    this.out.println("Portfolio created using file " + filePath + "!");
  }

  @Override
  public void showInvalidFilePath() {
    this.out.println("Invalid file path given! Please try again!");
  }

  @Override
  public void askDate() {
    this.out.println("Enter a valid date in the format yyyy-MM-dd:");
  }

  @Override
  public void showPortfolioDetailsNotAvailableForDate(String portfolioName, String date) {
    nullChecker.accept(new Object[]{portfolioName, date});
    this.out.println("Portfolio value for " + portfolioName + " on " + date
        + " is not available!");
  }

  @Override
  public void showStockDetailsNotAvailableOnDate(String ticker, String date) {
    nullChecker.accept(new Object[]{ticker, date});
    this.out.println("Stock data for " + ticker + " on " + date
        + " is not available!");
  }

  @Override
  public void showInitializationFailed(String fileName) {
    nullChecker.accept(new Object[]{fileName});
    this.out.println("Application startup failed! " + fileName + " could not be loaded!");
  }

  @Override
  public void showPortfolioNameCannotBeEmpty() {
    this.out.println("Portfolio name cannot be blank!");
  }

  @Override
  public void showPortfolioValue(String portfolioName, String date, Double portfolioValue) {
    this.out.println(
        "Value of Portfolio '" + portfolioName + "' on " + date + " is: " + portfolioValue);
  }

  @Override
  public void askCommissionChargeToSet() {
    this.out.println("Please enter the amount of commission to configure:");
  }

  @Override
  public void showInvalidNumberEntered(String kindOfInput) {
    this.out.println("Invalid value entered for " + kindOfInput);
  }

  @Override
  public void showInflexiblePortfolioMenu() {
    this.out.println("Choose from the below choices for Inflexible Portfolios:");
    this.out.println("Type '1' to create an Inflexible Portfolio");
    this.out.println("Type '2' to view composition of Inflexible Portfolio");
    this.out.println("Type '3' to view value of Inflexible Portfolio");
    this.out.println("Type 'back' to go back to the main menu");
  }

  @Override
  public void showFlexiblePortfolioMenu() {
    this.out.println("Choose from the below choices for Flexible Portfolios:");
    this.out.println("Type '1' to create a Flexible Portfolio");
    this.out.println("Type '2' to buy a stock by quantity");
    this.out.println("Type '3' to sell a stock");
    this.out.println("Type '4' to view cost basis of a Flexible Portfolio");
    this.out.println("Type '5' to view value of a Flexible Portfolio");
    this.out.println("Type '6' to view performance of a Flexible Portfolio");
    this.out.println("Type '7' to view composition of a Flexible Portfolio");
    this.out.println("Type '8' to buy stocks by amount");
    this.out.println("Type '9' to add a strategy to invest");
    this.out.println("Type 'back' to go back to the main menu");
  }

  @Override
  public void showError(String message) {
    this.out.println(message);
  }

  @Override
  public void showCostBasisOfPortfolio(String portfolioName, Double costBasis, String date) {
    this.out.println("Cost basis for " + portfolioName + " on " + date + " is " + costBasis);
  }

  @Override
  public void showPerformance(Map<String, Integer> scaledPerformances, ApiPeriod timeScale,
      double valueScale) {
    this.out.println();
    for (Map.Entry<String, Integer> entry : scaledPerformances.entrySet()) {
      if (timeScale.equals(ApiPeriod.MONTHLY)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(entry.getKey(), formatter);
        int year = date.getYear();
        String nameOfMonth = Month.from(date).name().substring(0, 3);
        this.out.print(nameOfMonth + " " + year + " : ");
      } else {
        this.out.print(entry.getKey() + " : ");
      }
      for (int i = 0; i < entry.getValue(); i++) {
        this.out.print("*");
      }
      this.out.println();
    }
    valueScale = Math.round(valueScale * 100.0) / 100.0;
    this.out.println("Scale of values : * = $" + valueScale);
    this.out.println("Scale of timestamps : " + timeScale);
  }

  @Override
  public void askFromDate() {
    this.out.println("Enter date from which you want to see performance ");
  }

  @Override
  public void askToDate() {
    this.out.println("Enter date upto which you want to see performance ");
  }

  @Override
  public void askFromDateForStrategy() {
    this.out.println("Enter date from which you want to implement strategy ");
  }

  @Override
  public void askToDateForStrategy() {
    this.out.println("Enter date upto which you want to implement strategy ");
  }

  @Override
  public void showPerformanceHeader(String portfolioName, String from, String to) {
    this.out.println("Performance of portfolio '" + portfolioName + "' from " + from + " to " + to);
  }

  @Override
  public void showTradeSuccessful(String tradeType) {
    this.out.println(tradeType + " performed successfully!");
  }

  @Override
  public void showCommissionChanged(Double commission) {
    this.out.println("Commission fess changed to " + commission);
  }

  @Override
  public void askDateForComposition(String portfolioName) {
    this.out.println("Please enter date on which you want to see the composition for Portfolio "
        + portfolioName);
  }

  @Override
  public void askProportionStock(String ticker) {
    this.out.println("Please enter the proportion of " + ticker);
  }

  @Override
  public void askAmountToBeInvested() {
    this.out.println("Please enter the amount you want to invest ");
  }

  @Override
  public void askPeriodForStrategy() {
    this.out.println("Please enter frequency to invest in strategy ");
    this.out.println("Type 'WEEKLY' to invest every week ");
    this.out.println("Type 'MONTHLY' to invest every month ");
  }

  @Override
  public void strategyFailed() {
    this.out.println("Could not create strategy!");
  }

  @Override
  public void strategyCreated() {
    this.out.println("Strategy created successfully!");
  }
}
