package controller;

import java.util.Map;
import java.util.Scanner;
import model.TypeOfPortfolio;
import model.User;
import view.StockAppView;

/**
 * Represents an implementation of {@link AppCommand} that enable all the features for inflexible
 * portfolio like creating it, showing its value and composition.
 */
final class InflexiblePortfolioCommand implements AppCommand {

  private final String RESOURCES_DIRECTORY;
  private final String API_KEY;
  private final Map<String, Map<String, Double>> stockPriceMap;

  /**
   * Constructs an instance of {@link InflexiblePortfolioCommand} that will enable all the features
   * of inflexible portfolio.
   *
   * @param stockPriceMap a map of maps of stock names and their respective values on a range of
   *                      dates
   * @param apiKey        for making the api calls to fetch data for stocks
   */
  public InflexiblePortfolioCommand(Map<String, Map<String, Double>> stockPriceMap,
      String resourcesDirectory, String apiKey) {
    this.stockPriceMap = stockPriceMap;
    this.RESOURCES_DIRECTORY = resourcesDirectory;
    this.API_KEY = apiKey;
  }

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    String userChoice;
    while (true) {
      view.showInflexiblePortfolioMenu();
      userChoice = sc.nextLine();
      AppCommand command = null;
      if (userChoice.equals("1")) {
        command = new PortfolioCreationCommand(stockPriceMap, RESOURCES_DIRECTORY, API_KEY);
      } else if (userChoice.equals("2")) {
        command = new StockCompositionCommand(TypeOfPortfolio.INFLEXIBLE);
      } else if (userChoice.equals("3")) {
        command = new StockValueCommand(stockPriceMap, TypeOfPortfolio.INFLEXIBLE);
      } else if (userChoice.equals("back")) {
        break;
      } else {
        view.showInvalidUserInput();
      }
      if (command != null) {
        command.execute(user, view, sc);
      }
    }
  }
}
