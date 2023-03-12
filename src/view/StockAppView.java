package view;

import java.util.List;
import java.util.Map;
import util.ApiPeriod;

/**
 * This interface represents a text based view of this application.
 */
public interface StockAppView {

  /**
   * Displays the stock application is starting.
   */
  void showAppStart();

  /**
   * Displays the stock application is being initialized.
   */
  void showInitialDataIsLoading();

  /**
   * Displays a message asking type of operations to be performed.
   */
  void showMainMenu();

  /**
   * Displays a message asking user to input the name of the portfolio.
   */
  void askNameOfPortfolio();

  /**
   * Displays a message that stock application is closing.
   */
  void showExitAppMessage();

  /**
   * Displays the entire composition of a portfolio.
   *
   * @param stocksAsString list of stocks to be displayed
   * @param portfolioName  name of portfolio to be displayed
   * @param total          value of portfolio on a given date
   * @param date           on which the details are being displayed
   */
  void showPortfolioDetails(List<String> stocksAsString, String portfolioName, Double total,
      String date);

  /**
   * Displays all the portfolio names.
   *
   * @param portfolioNames of the portfolio to be displayed.
   */
  void showAllPortfolioNames(List<String> portfolioNames);

  /**
   * Displays the initialization of the stock application is complete.
   */
  void showInitializationComplete();

  /**
   * Displays a message that input from the user is invalid.
   */
  void showInvalidUserInput();

  /**
   * Display options to create a portfolio either by file or manually.
   */
  void askWayToCreatePortfolio();

  /**
   * Asks the user to enter a Ticker symbol of the stock.
   */
  void askTickerOfStock();

  /**
   * Asks the user to enter quantity of the stock.
   *
   * @param stockName is name of stock
   */
  void askQuantityOfStock(String stockName);

  /**
   * Asks the user if they want to save this portfolio or want to add more stocks.
   */
  void askToSavePortfolio();

  /**
   * Displays that the entered ticker is not supported by the application.
   *
   * @param ticker name of the stock
   */
  void showUnsupportedTickerName(String ticker);

  /**
   * Displays that the quantity entered is not a valid quantity.
   */
  void showInvalidQuantityEntered();

  /**
   * Displays that the portfolio already exists.
   *
   * @param portfolioName of the portfolio
   */
  void showPortfolioAlreadyExists(String portfolioName);

  /**
   * Displays that the creation of the stock failed.
   */
  void showStockCreationUnsuccessful();

  /**
   * Displays that the portfolio creation failed.
   */
  void showPortfolioCreationUnsuccessful();

  /**
   * Displays that the portfolio doesn't exist with the given name.
   *
   * @param portfolioName is the name of portfolio
   */
  void showPortfolioDoesNotExist(String portfolioName);

  /**
   * Asks the user to enter a valid file path.
   */
  void askFilePathOfPortfolio();

  /**
   * Displays that the stock application cannot access the file.
   *
   * @param path is the filePath
   */
  void showFileNotPermitted(String path);

  /**
   * Displays that the portfolio cannot be loaded from the given file path.
   *
   * @param filePath is location where the file is located
   */
  void showLoadPortfolioByFileUnsuccessful(String filePath);

  /**
   * Displays that the portfolio is successfully loaded from the file.
   *
   * @param filePath is the path of the file
   */
  void showLoadPortfolioByFileSuccessful(String filePath);

  /**
   * Displays that the given file path is invalid.
   */
  void showInvalidFilePath();

  /**
   * Asks the user to enter a date in "YYYY/MM/DD" format.
   */
  void askDate();

  /**
   * Displays that the stock details are not available for the given date.
   *
   * @param portfolioName is the name for which details are to be displayed
   * @param date          is the date for which details are being asked for
   */
  void showPortfolioDetailsNotAvailableForDate(String portfolioName, String date);

  /**
   * Displays that the stock details are not available for given date.
   *
   * @param ticker is the name of the stock
   * @param date   is the date for which the data is not available
   */
  void showStockDetailsNotAvailableOnDate(String ticker, String date);

  /**
   * Displays that the initialization of the stock application failed.
   *
   * @param fileName is the name of the file that failed to load
   */
  void showInitializationFailed(String fileName);

  /**
   * Displays that the portfolio can't be created with null or blank name.
   */
  void showPortfolioNameCannotBeEmpty();

  /**
   * Displays the value of the portfolio.
   *
   * @param portfolioName  is the name of the portfolio
   * @param date           on which the value of portfolio is to be viewed
   * @param portfolioValue the value of the portfolio
   */

  void showPortfolioValue(String portfolioName, String date, Double portfolioValue);

  /**
   * Asks the user to enter the commission rate.
   */
  void askCommissionChargeToSet();


  /**
   * Displays a message that the number entered is invalid.
   *
   * @param kindOfInput is the type of input
   */
  void showInvalidNumberEntered(String kindOfInput);

  /**
   * Display the menu for the inflexible portfolio.
   */
  void showInflexiblePortfolioMenu();

  /**
   * Displays the menu for the flexible portfolio.
   */
  void showFlexiblePortfolioMenu();

  /**
   * Displays the error message.
   *
   * @param message is the message from the error
   */
  void showError(String message);

  /**
   * Displays the cost basis of the portfolio.
   *
   * @param portfolioName is the name of the portfolio
   * @param costBasis     is the value of the cost basis
   * @param date          is the date until which the cost basis is displayed
   */
  void showCostBasisOfPortfolio(String portfolioName, Double costBasis, String date);

  /**
   * Displays the performance of the portfolio as a bar graph.
   *
   * @param scaledPerformances represents the data needed for the bar graph
   * @param timeScale          is range for which the data is being displayed. It can be either
   *                           DAILY,WEEKLY,MONTHLY
   * @param valueScale         is the scaled value
   */
  void showPerformance(Map<String, Integer> scaledPerformances, ApiPeriod timeScale,
      double valueScale);

  /**
   * Asks the user to enter a valid date from which they want to view the performance.
   */
  void askFromDate();

  /**
   * Asks the user to enter a valid date up to which they want to view the performance.
   */
  void askToDate();

  void askFromDateForStrategy();

  void askToDateForStrategy();

  /**
   * Displays the header of the bar graph.
   *
   * @param portfolioName is the name of the portfolio
   * @param from          is date from which the portfolio data is being shown
   * @param to            is date up to which the portfolio data is being shown
   */
  void showPerformanceHeader(String portfolioName, String from, String to);

  /**
   * Displays that the trade has been successfully executed.
   *
   * @param tradeType is the type of trade i.e. BUY or SELL
   */
  void showTradeSuccessful(String tradeType);

  /**
   * Displays that the commission rate has changed.
   *
   * @param commission is the new rate of commission
   */
  void showCommissionChanged(Double commission);

  /**
   * Asks the user to enter a date for which they want to view the composition.
   *
   * @param portfolioName is the name of the portfolio
   */
  void askDateForComposition(String portfolioName);

  /**
   * Asks the user to input the proportion of stock to buy.
   *
   * @param ticker name of the stock
   */
  void askProportionStock(String ticker);

  /**
   * Asks the user to input the amount to invest in the portfolio.
   */
  void askAmountToBeInvested();

  /**
   * Asks the user to provide a from and a to date upto which investment needs to be made as part of
   * a strategy.
   */
  void askPeriodForStrategy();

  /**
   * Tells the user that strategy implementation failed.
   */
  void strategyFailed();

  /**
   * Tells the user that strategy implementation succeeded.
   */
  void strategyCreated();
}
