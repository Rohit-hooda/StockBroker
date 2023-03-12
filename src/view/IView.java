package view;

import controller.Feature;
import java.util.List;
import java.util.Map;
import util.ApiPeriod;

/**
 * Represents a graphical user interface that can perform various operations like buying or selling
 * of stocks, show its cost basis, show its performance graph, apply strategy and show its
 * composition.
 */

public interface IView {

  /**
   * Driver initializes this view to the StockAppGUIView and also populates the frame with main
   * initial frame.
   *
   * @param feature to be set from the driver
   */
  void addFeatures(Feature feature);

  /**
   * Creates a new frame and populate that frame with the name of the portfolios present in the
   * application.
   *
   * @param portfolioNames names of the portfolio already present
   */

  void populatePortfolioMenu(List<String> portfolioNames);

  /**
   * Adds a new portfolio to the portfolio drop down in the main panel.
   *
   * @param newPortfolioName is the name of new portfolio
   */
  void addNewPortfolioToMenu(String newPortfolioName);

  /**
   * Fetches the portfolio name.
   *
   * @return the portfolio name
   */
  String getNewPortfolioName();

  /**
   * Displays all the portfolio options available when portfolio is selected from the drop-down.
   */
  void showPortfolioOptions();

  /**
   * Redirects the application back to the main panel when Go Back Button is clicked.
   */
  void goBackToMainMenu();

  /**
   * Displays all the fields necessary to for getting the value of portfolio.
   */
  void showInputFormForPortfolioValue();

  /**
   * Fetches the inputs required for getting the value of the portfolio.
   *
   * @return a String of all the inputs required to fetch value
   */
  String getValueInputs();

  /**
   * Fetches the portfolio name on which operations are being performed.
   *
   * @return Portfolio name
   */
  String getCurrentPortfolioName();

  /**
   * Displays the value of the portfolio on the given date.
   *
   * @param value is the value of portfolio
   */
  void showValueOfPortfolio(Double value);

  /**
   * Displays all the fields necessary to for getting the composition of portfolio.
   */
  void showCompositionInputForm();

  /**
   * Fetches the inputs required for getting the composition of the portfolio.
   *
   * @return a String of all the inputs required to fetch composition
   */
  String getInputForComposition();

  /**
   * Displays the fetched composition data of the current portfolio.
   *
   * @param composition a map of the stock names and quantity
   */
  void showCompositionOfPortfolio(Map<String, Double> composition);

  /**
   * Displays all the fields necessary for buying the stocks by entering the quantity.
   */
  void showInputFormForBuyByQty();

  /**
   * Fetches the inputs required for getting the inputs for buying stocks by quantity.
   *
   * @return an array of strings of inputs
   */
  String[] getInputForBuyByQuantity();

  /**
   * Displays whether the buy was successful or not.
   *
   * @param ticker is the name of the stock
   */
  void showBuyStatus(String ticker);

  /**
   * Displays whether the stock was sold successfully or not.
   *
   * @param message is the error message
   */
  void showSellStatus(String message);

  /**
   * Displays the current commission charge in the dialog box.
   *
   * @param commissionCharge is the commission charge
   * @return the commission rate
   */
  Double getCommissionCharge(Double commissionCharge);

  /**
   * Displays all the fields necessary for selling the stocks.
   */
  void showInputFormForSell();

  /**
   * Fetches the inputs required to perform sell operation.
   *
   * @return an array of strings of inputs
   */
  String[] getInputForSell();

  /**
   * Displays all the fields necessary for computing the cost basis for the current portfolio.
   */
  void showInputFormForPortfolioCostBasis();

  /**
   * Displays the amount of money invested in the current portfolio.
   *
   * @param costBasisForFlexiblePortfolio is the amount of money invested
   */
  void showCostBasisOfPortfolio(Double costBasisForFlexiblePortfolio);

  /**
   * Fetches the inputs required for computing the cost basis for a given portfolio.
   *
   * @return a string value of inputs
   */
  String getCostBasisInputs();

  /**
   * Displays the value of the portfolio was not found.
   *
   * @param s is message that value was not found for the given portfolio
   */
  void showValueStatus(String s);

  /**
   * Displays the cost basis of the portfolio was not found.
   *
   * @param s is message that composition was not found for the given portfolio
   */
  void showCostBasisStatus(String s);

  /**
   * Displays all the fields necessary for buying stocks by specifying the amount of money to be
   * invested.
   */
  void showBuyByAmount();

  /**
   * Displays the date field necessary for getting the date.
   *
   * @return a string value of the date
   */
  String getDateToBuyByAmount();

  /**
   * Displays the amount field necessary for getting the amount of money to be invested.
   *
   * @return the amount of the money to be invested
   */
  Double getAmountToBuyByAmount();

  /**
   * Displays the number of stock field that is required to buy the stocks by specifying the
   * amount.
   *
   * @return a string value of the number of stocks
   */
  Integer getNumberOfStocksToBuy();

  /**
   * Displays all the fields required to get multiple stocks for buying by amount.
   *
   * @param numberOfStocks is the number of stocks.
   */
  void showInputForStocksWithProportion(Integer numberOfStocks);

  /**
   * Fetches the ticker name and proportion to be bought.
   *
   * @return map of ticker name and proportion
   */
  Map<String, Double> getTickerProportions();

  /**
   * Displays that the buy by amount operation failed.
   */
  void showBuyByAmountFailed();

  /**
   * Displays that the buy by amount operation succeeded.
   */
  void showBuyByAmountSucceeded();

  /**
   * Displays all the field required to implement a strategy.
   */
  void showStrategyInputs();

  /**
   * Fetches the from date required for performing buy using a strategy.
   *
   * @return from date
   */
  String getFromDateForStrategy();

  /**
   * Fetches the to date required to perform buy using a strategy.
   *
   * @return to date
   */
  String getToDateForStrategy();

  /**
   * Displays the amount text field required to get the amount to implement strategy.
   *
   * @return is the amount value to be invested
   */
  Double getAmountForStrategy();

  /**
   * Displays the number of stocks text field required to buy stocks using strategy.
   *
   * @return number of stocks that should be bought
   */
  Integer getNumberOfStocksForStrategy();

  /**
   * Displays the stock name and proportion field required to get inputs for implementing strategy.
   *
   * @param numberOfStocks is the number of stocks to be bought
   */
  void showInputForStocksWithProportionForStrategy(Integer numberOfStocks);

  /**
   * Fetches the ticker name and proportion to be bought by strategy.
   *
   * @return a map of stock name and proportion
   */
  Map<String, Double> getTickerProportionsForStrategy();

  /**
   * Displays that the strategy cannot be applied.
   */
  void showStrategyFailed();

  /**
   * Displays that the strategy was applied successfully.
   */

  void showStrategySucceeded();

  /**
   * Displays that the input dates were invalid.
   */
  void showInvalidFromToDates();

  /**
   * Fetches the frequency of investment. It can be either WEEKLY or MONTHLY.
   *
   * @return is the frequency of investment
   */
  String getPeriodForStrategy();

  /**
   * Displays the fields required to fetch the performance of the current portfolio.
   */
  void showPerformanceInputView();

  /**
   * Fetches the from date required for view the performance.
   *
   * @return from date
   */
  String getFromDateForPerformance();


  /**
   * Fetches the to date required for view the performance.
   *
   * @return to date
   */
  String getToDateForPerformance();

  /**
   * Displays the performance line graph of the performance of the current portfolio.
   *
   * @param performances is the performance of the portfolio overtime
   * @param period       is the period during which the performance is being showed
   */
  void showPerformanceView(Map<String, Double> performances, ApiPeriod period);
}
