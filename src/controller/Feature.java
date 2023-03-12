package controller;

import view.IView;

/**
 * Represents the features of the stocks-application.
 */
public interface Feature {

  /**
   * Sets the view to an IView.
   *
   * @param v IView instance
   */
  void setView(IView v);

  /**
   * Handles creation of a new portfolio.
   */
  void handleCreateNewPortfolio();

  /**
   * Enables user to choose from multiple options required for portfolio.
   */
  void handleWorkWithPortfolio();

  /**
   * Takes user out of the portfolio sub-menu.
   */
  void handleGoBack();

  /**
   * Handles feature of showing value of portfolio.
   */
  void handlePortfolioValue();

  /**
   * Tells the view to show the value of the portfolio.
   */
  void showPortfolioValue();

  /**
   * Handles feature of showing composition of portfolio.
   */
  void handleCompositionOfPortfolio();

  /**
   * Tells the view to show the composition of the portfolio.
   */
  void showPortfolioComposition();

  /**
   * Handles feature of buying a stock by specifying a quantity of portfolio.
   */
  void handleBuyByQuantity();

  /**
   * Takes inputs and asks model to perform buy a quantity of stocks.
   */
  void performBuyByQty();

  /**
   * Enables to change the commission fees in the application.
   */
  void handleChangeCommissionFee();

  /**
   * Enables feature to sell a specific number of stocks.
   */
  void handleSell();

  /**
   * Takes inputs and asks model to perform sell of a stock.
   */
  void performSell();

  /**
   * Handles feature of showing amount of money invested so far in a portfolio.
   */
  void handleCostBasis();

  /**
   * Tells the view to show amount of money invested so far in the portfolio.
   */
  void showCostBasis();

  /**
   * Handles feature of buying stocks by specifying an amount and proportions in a portfolio.
   */
  void handleBuyByAmount();

  /**
   * Tells UI to show inputs for taking n number of stocks.
   */
  void handleAddStocksToBuy();

  /**
   * Takes inputs and asks model to perform buy of a set of stocks with their proportions
   * specified.
   */
  void performBuyByAmount();

  /**
   * Handles feature of buying stocks by specifying an amount and proportions in a portfolio and
   * implementing it as a strategy periodically on a range of dates.
   */
  void handleStrategy();

  /**
   * Tells UI to show inputs for taking n number of stocks.
   */
  void handleAddStocksInStrategy();

  /**
   * Takes inputs and asks model to perform buy of a set of stocks with their proportions specified
   * and implement them as a strategy for a range of dates.
   */
  void implementStrategy();

  /**
   * Handles feature of showing performance of a portfolio over a given period of time.
   */
  void handlePerformance();

  /**
   * Tells the view to show the performance of a portfolio in suitable viewable form.
   */
  void showPerformance();
}
