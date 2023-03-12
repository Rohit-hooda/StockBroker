package controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import model.User;
import util.ApiPeriod;
import util.CsvFileUtil;
import util.FileUtil;
import util.ParamNullChecker;
import util.StockPriceUtil;
import util.StockUtil;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that handles the creation of flexible and
 * inflexible portfolio.
 */
final class PortfolioCreationCommand implements AppCommand {

  private static final String CSV = ".csv";
  private final String RESOURCES_DIRECTORY;
  private final String API_KEY;
  private final Consumer<Object[]> nullChecker;
  private final FileUtil fileUtil;
  private final StockUtil stockUtil;
  private final AppInputCommand appInputCommand;
  private Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of PortfolioCreationCommand that is responsible to co-ordinate with view
   * and model to enable creation of a portfolio by either a file or manually asking the stock
   * data.
   *
   * @param stockPriceMap      a map of maps of stock names and their respective values on a range
   *                           of dates
   * @param resourcesDirectory represents the base location of required resources for the
   *                           application to run
   * @param apiKey             to make api calls to fetch the stock data from api
   */
  public PortfolioCreationCommand(Map<String, Map<String, Double>> stockPriceMap,
      String resourcesDirectory, String apiKey) {
    nullChecker = new ParamNullChecker();
    this.stockPriceMap = stockPriceMap;
    this.RESOURCES_DIRECTORY = resourcesDirectory;
    this.API_KEY = apiKey;
    this.fileUtil = new CsvFileUtil();
    this.stockUtil = new StockPriceUtil();
    this.appInputCommand = new StockAppInputCommand();
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    nullChecker.accept(new Object[]{user, view, sc});
    view.askWayToCreatePortfolio();
    String wayToCreatePortfolio = sc.nextLine();
    while (!(wayToCreatePortfolio.equals("1") || wayToCreatePortfolio.equals("2"))) {
      view.showInvalidUserInput();
      view.askWayToCreatePortfolio();
      wayToCreatePortfolio = sc.nextLine();
    }
    if (wayToCreatePortfolio.equals("1")) {
      handlePortfolioCreationManually(user, view, sc);
    } else if (wayToCreatePortfolio.equals("2")) {
      handlePortfolioCreationByFile(user, view, sc);
    }
  }

  private void handlePortfolioCreationByFile(User user, StockAppView view, Scanner sc) {
    view.askFilePathOfPortfolio();
    String filePath = sc.nextLine();
    filePath = getValidFilePath(filePath, view, sc);
    File f = new File(filePath);
    String fileName = f.getName();
    String portfolioName = fileName.substring(0, fileName.length() - 4);
    try {
      List<String[]> stocks = fileUtil.readFile(filePath);
      Map<String, Double> stockMap = stockUtil.getStockMap(stocks, 0, 1);
      if (isAnyQuantityOfStockNegative(stockMap)) {
        throw new IllegalArgumentException("Quantity in a file for portfolio cannot be negative!");
      }
      stockPriceMap = stockUtil.fetchAllStockData(API_KEY, stockMap.keySet(),
          stockPriceMap);
      user.createPortfolio(stockMap, portfolioName);
      view.showLoadPortfolioByFileSuccessful(filePath);
      user.savePortfolio(portfolioName,
          RESOURCES_DIRECTORY + "inflexible_portfolios/", fileUtil);
    } catch (RuntimeException re) {
      view.showLoadPortfolioByFileUnsuccessful(filePath);
    }
  }

  private boolean isAnyQuantityOfStockNegative(Map<String, Double> stocks) {
    for (Map.Entry<String, Double> stock : stocks.entrySet()) {
      if (stock.getValue() < 0.0) {
        return true;
      }
    }
    return false;
  }

  private String getValidFilePath(String filePath, StockAppView view, Scanner sc) {
    while ((filePath.isBlank() || !filePath.contains(CSV))) {
      view.showInvalidFilePath();
      view.askFilePathOfPortfolio();
      filePath = sc.nextLine();
    }
    while (!isFilePermitted(filePath, view)) {
      view.showInvalidFilePath();
      view.askFilePathOfPortfolio();
      filePath = sc.nextLine();
    }
    return filePath;
  }

  private void handlePortfolioCreationManually(User user, StockAppView view, Scanner sc) {
    view.askNameOfPortfolio();
    String portfolioName = sc.nextLine();
    portfolioName = getNewPortfolioName(portfolioName, user, view, sc);
    String continueTakingStocks = "y";
    Map<String, Double> stocks = getMapOfStocksFromUser(continueTakingStocks, view, sc);
    try {
      user.createPortfolio(stocks, portfolioName);
    } catch (NullPointerException | IllegalArgumentException e) {
      view.showPortfolioCreationUnsuccessful();
    }
    user.savePortfolio(portfolioName,
        RESOURCES_DIRECTORY + "inflexible_portfolios/", fileUtil);
  }

  private Map<String, Double> getMapOfStocksFromUser(String continueTakingStocks, StockAppView view,
      Scanner sc) {
    Map<String, Double> stocks = new HashMap<>();
    while (!continueTakingStocks.equals("n")) {
      view.askTickerOfStock();
      String ticker = sc.nextLine();
      try {
        String stockPriceDataFileName = ticker + "_" + ApiPeriod.DAILY.getApiPeriod();
        if (!stockPriceMap.containsKey(stockPriceDataFileName)) {
          stockPriceMap = stockUtil.fetchStockData(API_KEY, ticker,
              stockPriceMap);
        }
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        view.showUnsupportedTickerName(ticker);
        continue;
      }
      view.askQuantityOfStock(ticker);
      String qtyStr = sc.nextLine();
      qtyStr = appInputCommand.getValidIntegerQuantity(ticker, qtyStr, view, sc);
      Double quantity = Double.parseDouble(qtyStr);
      stocks.put(ticker, stocks.getOrDefault(ticker, 0.0) + quantity);
      while (!continueTakingStocks.equals("n")) {
        view.askToSavePortfolio();
        continueTakingStocks = sc.nextLine();
        if (continueTakingStocks.equals("y")) {
          break;
        }
      }
    }
    return stocks;
  }


  private String getNewPortfolioName(String portfolioName, User user, StockAppView view,
      Scanner sc) {
    while (user.inflexiblePortfolioExists(portfolioName) || portfolioName.isBlank()) {
      if (portfolioName.isBlank()) {
        view.showPortfolioNameCannotBeEmpty();
      } else {
        view.showPortfolioAlreadyExists(portfolioName);
      }
      view.askNameOfPortfolio();
      portfolioName = sc.nextLine();
    }
    return portfolioName;
  }

  private boolean isFilePermitted(String filePath, StockAppView view) {
    File f = new File(filePath);
    boolean isValidPortfolioFile = false;
    try {
      isValidPortfolioFile = f.exists() && f.isFile();
    } catch (SecurityException se) {
      view.showFileNotPermitted(f.getPath());
    }
    return isValidPortfolioFile;
  }
}
