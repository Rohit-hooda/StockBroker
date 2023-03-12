package util;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single object and returns no result.
 */
public final class ParamNullChecker implements Consumer<Object[]> {

  /**
   * Checks if the given argument is null.
   *
   * @param objects the input argument
   * @throws NullPointerException when the given object is null
   */
  @Override
  public void accept(Object[] objects) throws NullPointerException {
    for (Object o : objects) {
      Objects.requireNonNull(o);
    }
  }
}
