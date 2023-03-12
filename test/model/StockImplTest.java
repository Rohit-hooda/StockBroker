package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link StockImpl} is correct.
 */
public class StockImplTest {

  private Stock stock;
  private String ticker;
  private Double quantity;

  @Before
  public void setUp() {
    quantity = Double.parseDouble("2");
    ticker = "AAPL";
    stock = new StockImpl(ticker, quantity);
  }

  @Test
  public void getName() {
    assertEquals(ticker, stock.getName());
  }

  @Test
  public void getQuantity() {
    assertEquals(quantity, stock.getQuantity(), 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingStockWithNullStockName() {
    stock = new StockImpl(null, quantity);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingStockWithNullStockQty() {
    stock = new StockImpl(ticker, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionCreatingStockWithZeroStockQty() {
    stock = new StockImpl(ticker, 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionCreatingStockWithEmptyStockName() {
    stock = new StockImpl("", quantity);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionCreatingStockWithNullValues() {
    stock = new StockImpl(null, null);
  }

  @Test
  public void shouldCheckEqualStocks() {
    Stock stock1 = new StockImpl("teststock", quantity);
    Stock stock2 = new StockImpl("teststock", quantity);

    assertNotSame(stock1, stock2);
    assertEquals(stock1, stock2);
    assertEquals(stock2, stock1);
    assertEquals(stock1.hashCode(), stock2.hashCode());
  }

  @Test
  public void shouldCheckImmutabilityOfQuantityOfStock() {
    quantity = 3.0;
    Double actualQty = stock.getQuantity();

    assertNotSame(quantity, actualQty);
    assertNotEquals(quantity, actualQty);
    assertNotEquals(actualQty, quantity);
    assertNotEquals(quantity.hashCode(), actualQty.hashCode());
  }

  @Test
  public void shouldCheckImmutabilityOfNameOfStock() {
    ticker = "stock2";
    String actualTicker = stock.getName();

    assertNotSame(ticker, actualTicker);
    assertNotEquals(ticker, actualTicker);
    assertNotEquals(actualTicker, ticker);
    assertNotEquals(ticker.hashCode(), actualTicker.hashCode());
  }

  @Test
  public void shouldCheckUnequalStocks() {
    Stock stock1 = new StockImpl("teststock1", quantity);
    Stock stock2 = new StockImpl("teststock2", quantity + 1.0);

    assertNotSame(stock1, stock2);
    assertNotEquals(stock1, stock2);
    assertNotEquals(stock2, stock1);
    assertNotEquals(stock1.hashCode(), stock2.hashCode());
  }

}