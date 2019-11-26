package net.techtrix.wee.lib;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepeatingAttemptTest {

  public static final int ONE = 1;

  @Test
  void attemptWithDefaultValues() {
    RepeatingAttempt<Double> repeatingAttempt = RepeatingAttempt.of(Math::random).until(aDouble -> true).go();

    assertTrue(repeatingAttempt.isStarted());
    assertTrue(repeatingAttempt.isFinished());
    assertTrue(repeatingAttempt.isSuccessful());
    assertTrue(repeatingAttempt.getResult().isPresent());
    assertEquals(ONE, repeatingAttempt.getNrAttempts());
  }

  @Test
  void attemptWithDefaultValuesAndMultipleTries() {
    final AtomicInteger counter = new AtomicInteger(0);
    final int expectedAttempts = 3;

    RepeatingAttempt<Double> repeatingAttempt = RepeatingAttempt.of(Math::random).until(aDouble -> {
      int count = counter.incrementAndGet();

      return count >= expectedAttempts;
    }).go();

    assertTrue(repeatingAttempt.isStarted());
    assertTrue(repeatingAttempt.isFinished());
    assertTrue(repeatingAttempt.isSuccessful());
    assertTrue(repeatingAttempt.getResult().isPresent());
    assertEquals(expectedAttempts, repeatingAttempt.getNrAttempts());
  }

  @Test
  void attemptWithDelayAndFixedRetriesThatFailsExceedingRetries() {
    final AtomicInteger counter = new AtomicInteger(0);
    final int attemptsBeforeResult = 3;
    final int expectedAttempts = attemptsBeforeResult - 1;

    RepeatingAttempt<Double> repeatingAttempt =
        RepeatingAttempt.of(Math::random)
            .until(aDouble -> {
              int count = counter.incrementAndGet();

              return count >= attemptsBeforeResult;
            })
            .fixedDelay(Duration.ofMillis(10))
            .abortingAfter(attemptsBeforeResult - 1)
            .go();

    assertTrue(repeatingAttempt.isStarted());
    assertTrue(repeatingAttempt.isFinished());
    assertFalse(repeatingAttempt.isSuccessful());
    assertFalse(repeatingAttempt.getResult().isPresent());
    assertEquals(expectedAttempts, repeatingAttempt.getNrAttempts());
  }

}