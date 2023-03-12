package controller;

import java.util.Scanner;
import model.User;
import view.StockAppView;

/**
 * Represents various commands aligned to features of the application like creating portfolio,
 * showing its value and composition etc.
 */
interface AppCommand {

  /**
   * Performs the operation for a feature of the application.
   *
   * @param user is main model of application
   * @param view is the view interface of the application
   * @param sc   scanner to take inputs from interface
   */
  void execute(User user, StockAppView view, Scanner sc);
}
