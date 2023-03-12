package util;

import java.util.function.BiFunction;

/**
 * Represents a function that accepts in list of strings and deli-metre to produce a single string
 * append with delimiter.
 */
public final class StringAppender implements BiFunction<String[], String, String> {

  /**
   * Takes a list of string and appends them with a deli-metre and the return as a string.
   *
   * @param strings   list of string to be appended
   * @param delimiter for list of strings to be appended with
   * @return a single string
   */
  @Override
  public String apply(String[] strings, String delimiter) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < strings.length - 1; i++) {
      sb.append(strings[i]);
      sb.append(delimiter);
    }
    sb.append(strings[strings.length - 1]);
    return sb.toString();
  }
}
