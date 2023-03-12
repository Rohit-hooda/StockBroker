package util;

import static org.junit.Assert.assertTrue;

import java.util.Scanner;
import java.util.function.Function;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link FileScannerFunction} util class is
 * correct.
 */
public class FileScannerFunctionTest {

  @Test
  public void apply() {
    Function<String, Scanner> fileScanner = new FileScannerFunction();
    Scanner actual = fileScanner.apply("test/resources/TestPortfolio.csv");

    assertTrue(actual.hasNextLine());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenFileNotPresent() {
    Function<String, Scanner> fileScanner = new FileScannerFunction();
    Scanner actual = fileScanner.apply("test2/resources/TestPortfolio.csv");
  }
}