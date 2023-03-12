package util;

/**
 * This enumerated type represents the period for the API.
 */
public enum ApiPeriod {
  DAILY("DAILY"),
  WEEKLY("WEEKLY"),
  MONTHLY("MONTHLY"),
  YEARLY("YEARLY");

  private final String period;

  ApiPeriod(String period) {
    this.period = period;
  }

  /**
   * Fetches the value for the API period.
   *
   * @param otherPeriod represents the other period
   * @return the API period
   */
  public static ApiPeriod valueOfApiPeriod(String otherPeriod) {
    for (ApiPeriod apiPeriod : values()) {
      if (apiPeriod.period.equalsIgnoreCase(otherPeriod)) {
        return apiPeriod;
      }
    }
    return null;
  }

  /**
   * Checks if the given API period is valid API period.
   *
   * @param otherPeriod is the other period
   * @return true if the API period is valid and false if it's not
   */
  public static boolean isAnApiPeriod(String otherPeriod) {
    for (ApiPeriod apiPeriod : values()) {
      if (apiPeriod.period.equalsIgnoreCase(otherPeriod)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Fetches the API period.
   *
   * @return the period of the API
   */
  public String getApiPeriod() {
    return period;
  }
}
