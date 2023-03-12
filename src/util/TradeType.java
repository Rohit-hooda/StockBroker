package util;

/**
 * This enumerated type represents the type of trade that can be performed.
 */
public enum TradeType {
  BUY("BUY"),
  SELL("SELL");

  private final String type;

  TradeType(String type) {
    this.type = type;
  }

  /**
   * Fetches the value for the trade type.
   *
   * @param otherTradeType represents the other trade type
   * @return the type of Trade
   */
  public static TradeType valueOfTradeType(String otherTradeType) {
    for (TradeType tradeType : values()) {
      if (tradeType.type.equals(otherTradeType)) {
        return tradeType;
      }
    }
    return null;
  }

  /**
   * Checks if the given trade type is valid type or not.
   *
   * @param otherTradeType represents the trade type
   * @return
   */
  public static boolean isTradeType(String otherTradeType) {
    for (TradeType tradeType : values()) {
      if (tradeType.type.equals(otherTradeType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Fetches the type of the tade.
   *
   * @return type of the trade
   */
  public String getTradeType() {
    return type;
  }
}
