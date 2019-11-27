package net.techtrix.wee.lib;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CachedTest {

  public static final int ONCE = 1;
  public static final int TWICE = 2;

  @Test
  void testSimpleCreation() {
    Supplier<Double> randomDoubleSupplier = spyRandomSupplier();

    Cached<Double> cachedRandomDouble = Cached.of(randomDoubleSupplier);
    cachedRandomDouble.get();
    Double result = cachedRandomDouble.get();

    assertNotNull(result);
    verify(randomDoubleSupplier, times(ONCE)).get();
  }

  @Test
  void testCreationWithFrequentInvalidation() {
    Supplier<Double> randomDoubleSupplier = spyRandomSupplier();

    Duration retentionPeriod = Duration.ofMillis(10);
    Cached<Double> cachedRandomDouble =
        Cached.of(randomDoubleSupplier)
            .retainingFor(retentionPeriod);

    Double result = cachedRandomDouble.get();
    assertNotNull(result);

    waitFor(retentionPeriod.plus(10, ChronoUnit.MILLIS));

    result = cachedRandomDouble.get();
    assertNotNull(result);
    verify(randomDoubleSupplier, times(TWICE)).get();
  }

  @Test
  void testThreadSafety() throws ExecutionException, InterruptedException {
    Supplier<Double> randomDoubleSupplier = spyRandomSupplier();
    Cached<Double> cachedRandomDouble = Cached.of(randomDoubleSupplier);

    final int numThreads = 8;
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    executor.submit(() ->
        IntStream.range(0, numThreads)
            .parallel()
            .forEach(i -> {
              cachedRandomDouble.get();
            }))
        .get();

    verify(randomDoubleSupplier, times(ONCE)).get();

  }

  private Supplier<Double> spyRandomSupplier() {
    return spy(new Supplier<Double>() {
      @Override
      public Double get() {
        return Math.random();
      }
    });
  }

  private void waitFor(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (Exception e) {
    }
  }

}