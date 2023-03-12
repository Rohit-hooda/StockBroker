package model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Represents a test class for {@link TradeImpl}.
 */
public class TradeImplTest {

  private Stock stock;
  private Trade trade;

  @Before
  public void setUp() throws Exception {
    stock = new MockStock("Stock1", 1.0);
  }

  @Test
  public void shouldCreateTrade() {
    trade = new TradeImpl(stock, "2022-11-09");

    assertEquals("2022-11-09", trade.getDateOfTrade());
    assertEquals("Stock1", trade.getName());
    assertEquals(1.0, trade.getQuantity(), 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenCreateTradeWithNullStock() {
    Trade trade = new TradeImpl(null, "2022-11-09");
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenCreateTradeWithNullDate() {
    Stock stock = new MockStock("Stock1", 1.0);
    Trade trade = new TradeImpl(stock, null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionWhenCreateTradeWithNullValues() {
    Trade trade = new TradeImpl(null, null);
  }

  @Test
  public void getName() {
    trade = new TradeImpl(stock, "2022-11-09");

    assertEquals("Stock1", trade.getName());
  }

  @Test
  public void getQuantity() {
    trade = new TradeImpl(stock, "2022-11-09");

    assertEquals(1.0, trade.getQuantity(), 0.0);
  }

  @Test
  public void getQuantityWhenNegative() {
    stock = new MockStock("Stock1", -1.0);
    trade = new TradeImpl(stock, "2022-11-09");

    assertEquals(-1.0, trade.getQuantity(), 0.0);
  }

  @Test
  public void getDateOfTrade() {
    trade = new TradeImpl(stock, "2022-11-09");

    assertEquals("2022-11-09", trade.getDateOfTrade());
  }

  static class MockStock implements Stock {

    private final String name;
    private final Double quantity;

    public MockStock(String name, Double quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public Double getQuantity() {
      return this.quantity;
    }
  }
}