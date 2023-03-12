package controller;

import java.util.Scanner;
import model.User;
import view.StockAppView;

/**
 * Accomplishes feature of setting the commission charge in the application.
 */
final class CommissionChargeCommand implements AppCommand {

  @Override
  public void execute(User user, StockAppView view, Scanner sc) {
    view.askCommissionChargeToSet();
    String commissionStr = sc.nextLine();
    commissionStr = getValidInteger(commissionStr, view, sc);
    Double commission = Double.parseDouble(commissionStr);
    user.setCommissionCharge(commission);
    view.showCommissionChanged(commission);
  }

  private String getValidInteger(String commission, StockAppView view, Scanner sc) {
    while (!isValidInteger(commission, view)) {
      view.askCommissionChargeToSet();
      commission = sc.nextLine();
    }
    return commission;
  }

  private boolean isValidInteger(String qtyStr, StockAppView view) {
    try {
      Double.parseDouble(qtyStr);
      if (qtyStr.charAt(0) == '-') {
        throw new IllegalArgumentException("Commission cannot be negative");
      }
      return true;
    } catch (IllegalArgumentException nfe) {
      view.showInvalidNumberEntered("Commission");
    }
    return false;
  }
}
