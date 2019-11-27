package io.techtrix.wee.lib;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LazyTest {

  public static final int ZERO = 0;
  public static final int ONE = 1;

  @Test
  void testCreation() {
    Supplier<Double> spiedRandomSupplier = getSpiedTestSupplier();

    Lazy<Double> result = Lazy.of(spiedRandomSupplier);

    assertNotNull(result);
    verify(spiedRandomSupplier, times(ZERO)).get();
  }

  @Test
  void testGetSingleThread() {
    Supplier<Double> spiedRandomSupplier = getSpiedTestSupplier();

    Lazy<Double> lazyResult = Lazy.of(spiedRandomSupplier);

    assertNotNull(lazyResult);

    Double actualResult = lazyResult.get();
    verify(spiedRandomSupplier, times(ONE)).get();
    assertNotNull(actualResult);
  }

  @Test
  void testGetMultiThread() {
    Supplier<Double> spiedRandomSupplier = getSpiedTestSupplier();

    Lazy<Double> lazyResult = Lazy.of(spiedRandomSupplier);

    assertNotNull(lazyResult);

    long nrValuesFetched =
        IntStream
            .range(1, 100)
            .parallel()
            .mapToObj(it -> lazyResult.get())
            .distinct()
            .count();

    verify(spiedRandomSupplier, times(ONE)).get();
    assertEquals(ONE, nrValuesFetched);
  }

  private Supplier<Double> getSpiedTestSupplier() {
    Supplier<Double> randomSupplier = new Supplier<Double>() {
      @Override
      public Double get() {
        return Math.random();
      }
    };
    return Mockito.spy(randomSupplier);
  }
}