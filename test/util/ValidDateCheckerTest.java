package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link ValidDateChecker} util class is correct.
 */
public class ValidDateCheckerTest {

  @Test
  public void apply() {
    Function<String, Boolean> validDateChecker = new ValidDateChecker();

    assertTrue(validDateChecker.apply("2022-10-10"));
    assertFalse(validDateChecker.apply("2025-10-10"));
    assertFalse(validDateChecker.apply("2025/10/10"));
    assertFalse(validDateChecker.apply("abc"));
  }
}