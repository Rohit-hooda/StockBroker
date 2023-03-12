package model;

import java.util.Objects;
import java.util.function.Consumer;
import util.ParamNullChecker;

/**
 * Represents an implementation of {@link Stock} that can have a ticker name and valid quantity.
 */
final class StockImpl implements Stock {

  private final String name;
  private final Double quantity;

  /**
   * Constructs a Stock with a non-null non-empty ticker name and a quantity.
   *
   * @param name     ticker name of the stock
   * @param quantity quantity of stocks
   * @throws NullPointerException     if name or quantity are null
   * @throws IllegalArgumentException if stock name is empty or blank or quantity is zero
   */
  StockImpl(String name, Double quantity)
      throws NullPointerException, IllegalArgumentException {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{name, quantity});
    if (isInvalidString(name)) {
      throw new IllegalArgumentException("Stock name cannot be empty!");
    }
    this.name = name;
    if (!isValidQuantity(quantity)) {
      throw new IllegalArgumentException("Quantity cannot be zero!");
    }
    this.quantity = quantity;
  }

  private boolean isValidQuantity(Double quantity) {
    return !quantity.equals(0.0);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Double getQuantity() {
    return this.quantity;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockImpl stock = (StockImpl) o;
    return name.equals(stock.name) && quantity.equals(stock.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, quantity);
  }

  private boolean isInvalidString(String string) {
    return string.isBlank() || string.isEmpty();
  }
}
