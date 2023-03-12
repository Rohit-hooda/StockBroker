package util;

import java.util.function.Consumer;
import model.UserImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Represents a Test class to test if behaviour of {@link ParamNullChecker} util class is correct.
 */
public class ParamNullCheckerTest {

  @Test
  public void accept() {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    Object o1 = new UserImpl();
    Object o2 = System.in;
    nullChecker.accept(new Object[]{o1, o2});

    Assert.assertNotNull(o1);
    Assert.assertNotNull(o2);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionIfNullParamDetected() {
    Consumer<Object[]> nullChecker = new ParamNullChecker();
    nullChecker.accept(new Object[]{null, null});
  }
}