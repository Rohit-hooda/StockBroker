package model;

import java.util.function.Consumer;
import util.ParamNullChecker;

/**
 * Represents an implementation of {@link Trade} that can have a stock details and date of trade.
 */
final class TradeImpl implements Trade {

  private final Stock stock;
  private final String date;

  /**
   * Constructs a Trade with stock details and a positive quantity.
   *
   * @param stock stock traded
   * @param date  of the trade
   * @throws NullPointerException if stock or date are null
   */
  public TradeImpl(Stock stock, String date) {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{stock, date});
    this.stock = stock;
    this.date = date;
  }

  @Override
  public String getName() {
    return this.stock.getName();
  }

  @Override
  public Double getQuantity() {
    return this.stock.getQuantity();
  }

  @Override
  public String getDateOfTrade() {
    return this.date;
  }
}
