package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import util.ApiPeriod;
import util.FileUtil;
import util.ParamNullChecker;

/**
 * Represents an implementation of the {@link User} interface that has the ability to create, store,
 * get composition and get value of portfolios with the user.
 */
public final class UserImpl implements User {

  private final Map<String, Portfolio> inflexiblePortfolioMap;
  private final Map<String, FlexiblePortfolio> flexiblePortfolioMap;
  private final Consumer<Object[]> nullChecker;

  private Double commission;

  /**
   * Constructs an instance of user and initialises map to store the portfolios.
   */
  public UserImpl() {
    inflexiblePortfolioMap = new HashMap<>();
    flexiblePortfolioMap = new HashMap<>();
    nullChecker = new ParamNullChecker();
    commission = 10.0;
  }

  @Override
  public void createPortfolio(Map<String, Double> stocksMap, String portfolioName)
      throws NullPointerException, IllegalArgumentException {
    nullChecker.accept(new Object[]{portfolioName});
    if (inflexiblePortfolioExists(portfolioName) || flexiblePortfolioExists(portfolioName)) {
      throw new IllegalArgumentException("Portfolio with same name exists!");
    }
    if (stocksMap == null) {
      flexiblePortfolioMap.put(portfolioName, new FlexiblePortfolioImpl(portfolioName));
    } else {
      List<Stock> stocks = new ArrayList<>();
      for (Map.Entry<String, Double> stockData : stocksMap.entrySet()) {
        Stock stock = new StockImpl(stockData.getKey(), stockData.getValue());
        stocks.add(stock);
      }
      inflexiblePortfolioMap.put(portfolioName, new InflexiblePortfolioImpl(portfolioName, stocks));
    }
  }

  @Override
  public void addTradeToFlexiblePortfolio(String portfolioName, String ticker, Double quantity,
      String date) {
    nullChecker.accept(new Object[]{portfolioName, ticker, quantity, date});
    if (flexiblePortfolioMap.containsKey(portfolioName)) {
      FlexiblePortfolio flexiblePortfolio = flexiblePortfolioMap.get(portfolioName);
      flexiblePortfolio.addTrade(ticker, quantity, date);
      flexiblePortfolioMap.put(portfolioName, flexiblePortfolio);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist with name: " + portfolioName);
    }
  }

  @Override
  public void addFractionalTradeToFlexiblePortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String date) {
    nullChecker.accept(new Object[]{portfolioName, amount, tickerRatios, date});
    if (flexiblePortfolioMap.containsKey(portfolioName)) {
      FlexiblePortfolio flexiblePortfolio = flexiblePortfolioMap.get(portfolioName);
      flexiblePortfolio.addFractionalTrades(stockPriceMap, amount, tickerRatios, date);
      flexiblePortfolioMap.put(portfolioName, flexiblePortfolio);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist with name: " + portfolioName);
    }
  }

  @Override
  public Double getCommissionCharge() {
    return this.commission;
  }

  @Override
  public void setCommissionCharge(Double commission) {
    this.commission = commission;
  }

  @Override
  public Double getCostBasisForFlexiblePortfolio(String portfolioName, String date,
      Map<String, Map<String, Double>> stockPriceMap) {
    nullChecker.accept(new Object[]{portfolioName, date});
    if (flexiblePortfolioMap.containsKey(portfolioName)) {
      return flexiblePortfolioMap.get(portfolioName).getCostBasis(date, commission, stockPriceMap);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist with name: " + portfolioName);
    }
  }

  @Override
  public Map<String, Double> getCompositionOfPortfolio(String portfolioName, String date)
      throws IllegalArgumentException {
    nullChecker.accept(new Object[]{portfolioName});
    return getPortfolio(portfolioName).getComposition(date);
  }

  @Override
  public List<String> listAllInflexiblePortfolioNames() {
    return new ArrayList<>(inflexiblePortfolioMap.keySet());
  }

  @Override
  public List<String> listAllFlexiblePortfolioNames() {
    return new ArrayList<>(flexiblePortfolioMap.keySet());
  }


  @Override
  public boolean inflexiblePortfolioExists(String portfolioName) throws NullPointerException {
    nullChecker.accept(new Object[]{portfolioName});
    return inflexiblePortfolioMap.containsKey(portfolioName);
  }

  @Override
  public boolean flexiblePortfolioExists(String portfolioName) throws NullPointerException {
    nullChecker.accept(new Object[]{portfolioName});
    return flexiblePortfolioMap.containsKey(portfolioName);
  }

  @Override
  public void savePortfolio(String portfolioName, String filePath,
      FileUtil fileUtil) {
    nullChecker.accept(new Object[]{portfolioName, fileUtil});
    getPortfolio(portfolioName).savePortfolio(filePath, fileUtil);
  }

  private Portfolio getPortfolio(String portfolioName) {
    if (inflexiblePortfolioMap.containsKey(portfolioName)) {
      return inflexiblePortfolioMap.get(portfolioName);
    } else if (flexiblePortfolioMap.containsKey(portfolioName)) {
      return flexiblePortfolioMap.get(portfolioName);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist with name: " + portfolioName);
    }
  }

  @Override
  public Map<String, List<String[]>> loadExistingPortfolio(
      FileUtil fileUtil,
      String filePath) {
    return fileUtil.readAllFiles(filePath);
  }

  @Override
  public void getPerformanceOfFlexiblePortfolio(String portfolioName, String from, String to,
      Map<String, Map<String, Double>> stockPriceMap,
      BiConsumer<Map<String, Double>, ApiPeriod> performanceScaler) {
    nullChecker.accept(new Object[]{portfolioName, from, to, stockPriceMap, performanceScaler});
    if (flexiblePortfolioMap.containsKey(portfolioName)) {
      flexiblePortfolioMap.get(portfolioName)
          .getPerformance(from, to, stockPriceMap, performanceScaler);
    }
  }

  @Override
  public void addStrategyToFlexiblePortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockPriceMap, Double amount,
      Map<String, Double> tickerRatios, String from, String to, ApiPeriod period)
      throws IllegalArgumentException {
    nullChecker.accept(new Object[]{portfolioName, amount, tickerRatios, from, period});
    if (flexiblePortfolioMap.containsKey(portfolioName)) {
      FlexiblePortfolio flexiblePortfolio = flexiblePortfolioMap.get(portfolioName);
      flexiblePortfolio.addStrategy(stockPriceMap, amount, tickerRatios, from, to, period);
      flexiblePortfolioMap.put(portfolioName, flexiblePortfolio);
    } else {
      throw new IllegalArgumentException("Portfolio does not exist with name: " + portfolioName);
    }
  }

  @Override
  public Double getValuesOfStocksOfPortfolio(String portfolioName,
      Map<String, Map<String, Double>> stockPriceMap, String date)
      throws IllegalArgumentException, NullPointerException {
    nullChecker.accept(new Object[]{portfolioName, stockPriceMap, date});
    return getPortfolio(portfolioName).getValue(stockPriceMap, date);
  }
}
