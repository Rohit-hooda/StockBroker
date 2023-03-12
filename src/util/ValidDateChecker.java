package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

/**
 * This class represents a function that accepts a date and produces a boolean value.
 */
public final class ValidDateChecker implements Function<String, Boolean> {

  private static final SimpleDateFormat FORMATTER1 = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Applies this function to the given date string.
   *
   * @param dateString is the given value of the date
   * @return a boolean value if the date is valid
   */
  @Override
  public Boolean apply(String dateString) {
    try {
      LocalDate date = LocalDate.parse(dateString, FORMATTER2);
      FORMATTER1.parse(dateString);
      if (date.isAfter(LocalDate.now())) {
        return false;
      }
    } catch (ParseException | DateTimeParseException e) {
      return false;
    }
    return true;
  }
}
