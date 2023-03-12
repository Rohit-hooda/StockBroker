package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a Scanner.
 */
public final class FileScannerFunction implements Function<String, Scanner> {

  /**
   * Applies this function to a given string.
   *
   * @param fileName name of the file
   * @return an instance of Scanner for the given fileName
   * @throws IllegalArgumentException when the given file is not found
   */
  @Override
  public Scanner apply(String fileName) throws IllegalArgumentException {
    try {
      return new Scanner(new File(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found!");
    }
  }
}
