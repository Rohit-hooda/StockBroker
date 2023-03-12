package model;

/**
 * Represents all kinds of portfolios possible in the application.
 */
public enum TypeOfPortfolio {
  FLEXIBLE("FLEXIBLE"),
  INFLEXIBLE("INFLEXIBLE");

  private final String type;

  TypeOfPortfolio(String type) {
    this.type = type;
  }

  /**
   * Fetches the TypeOfPortfolio if it matches to any of existing TypeOfPortfolios.
   *
   * @param otherType string value of other typeofportfolio
   * @return enum instance of TypeOfPortfolio if finds a match else return null
   */
  public static TypeOfPortfolio valueOfTypeOfPortfolio(String otherType) {
    for (TypeOfPortfolio typeOfPortfolio : values()) {
      if (typeOfPortfolio.type.equals(otherType)) {
        return typeOfPortfolio;
      }
    }
    return null;
  }

  /**
   * Checks if string provided matched one of the TypeOfPortfolios present.
   *
   * @param otherType string value of other typeofportfolio
   * @return true if string matches existing values else returns false
   */
  public static boolean isATypeOfPortfolio(String otherType) {
    for (TypeOfPortfolio typeOfPortfolio : values()) {
      if (typeOfPortfolio.type.equals(otherType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns actual string value of TypeOf Portfolio enum instance.
   *
   * @return string vsalue of enum
   */
  public String getTypeOfPortfolio() {
    return type;
  }
}
