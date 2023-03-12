package model;

/**
 * Represents a Trade that has happened for a particular stock on a particular day.
 */
interface Trade extends Stock {

  /**
   * Fetches the date on which trade took place.
   *
   * @return date of trade
   */
  String getDateOfTrade();

}
