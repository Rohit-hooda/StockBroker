package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import util.FileUtil;
import util.ParamNullChecker;

/**
 * Represents an implementation of {@link Portfolio} that can store a collection of stocks and has a
 * name and stocks cannot be changed once added.
 */
final class InflexiblePortfolioImpl extends AbstractPortfolio {

  private final String portfolioName;
  private final List<Stock> stockList;

  /**
   * Constructs an instance of Portfolio that has a name and a non-empty collection of stocks.
   *
   * @param portfolioName name of the portfolio
   * @param stockList     is a list of stocks to be contained in the portfolio
   * @throws NullPointerException     if name or list of stocks are null
   * @throws IllegalArgumentException list of stocks is empty
   */
  public InflexiblePortfolioImpl(String portfolioName, List<Stock> stockList)
      throws NullPointerException, IllegalArgumentException {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{portfolioName, stockList});
    if (stockList.size() == 0) {
      throw new IllegalArgumentException("List of stocks in portfolio cannot be empty!");
    }
    this.portfolioName = portfolioName;
    this.stockList = new ArrayList<>(stockList);
  }

  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  @Override
  public Map<String, Double> getComposition(String date) {
    Map<String, Double> stockMap = new HashMap<>();
    for (Stock stock : this.stockList) {
      stockMap.put(stock.getName(), stock.getQuantity());
    }
    return stockMap;
  }

  @Override
  public void savePortfolio(String filePath, FileUtil fileUtil) {
    List<String[]> itemList = new LinkedList<>();
    itemList.add(new String[]{"Stock Name", "Quantity"});
    for (Stock stock : stockList) {
      itemList.add(new String[]{stock.getName(), stock.getQuantity() + ""});
    }
    fileUtil.savePortfolio(itemList, portfolioName, filePath);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InflexiblePortfolioImpl portfolio = (InflexiblePortfolioImpl) o;
    return portfolioName.equals(portfolio.portfolioName) && stockList.equals(portfolio.stockList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portfolioName, stockList);
  }
}
