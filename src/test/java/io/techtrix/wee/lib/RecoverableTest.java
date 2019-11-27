package io.techtrix.wee.lib;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecoverableTest {

  @Test
  void testWithoutNeedForRecovery() {
    final int expectedValue = 5;
    Recoverable<Integer> recoverableInt = Recoverable.of(() -> expectedValue).go();

    assertTrue(recoverableInt.isExecuted());
    assertTrue(recoverableInt.getExecutionTimestamp().isPresent());
    assertTrue(recoverableInt.isSuccessful());
    assertFalse(recoverableInt.getRecoveryCause().isPresent());
    assertFalse(recoverableInt.getFailureCause().isPresent());
    assertEquals(expectedValue, recoverableInt.getResult());
  }

  @Test
  void testSuccessfulWithFinally() {
    final int expectedValue = 5;
    AtomicBoolean execFlag = new AtomicBoolean(false);

    Recoverable<Integer> recoverableInt =
        Recoverable
            .of(() -> expectedValue)
            .finallyDo(() -> execFlag.set(true))
            .go();

    assertTrue(recoverableInt.isExecuted());
    assertTrue(execFlag.get());
    assertTrue(recoverableInt.getExecutionTimestamp().isPresent());
    assertTrue(recoverableInt.isSuccessful());
    assertFalse(recoverableInt.getRecoveryCause().isPresent());
    assertFalse(recoverableInt.getFailureCause().isPresent());
    assertEquals(expectedValue, recoverableInt.getResult());
  }

  @Test
  void testRecoveryWithFinally() {
    final int fallbackValue = 5;
    AtomicBoolean execFlag = new AtomicBoolean(false);

    Recoverable<Integer> recoverableInt =
        Recoverable.<Integer>of(() -> {
          throw new IllegalStateException("Cannot supply value");
        })
            .recoverIf(IllegalStateException.class, fallbackValue)
            .finallyDo(() -> execFlag.set(true))
            .go();

    assertTrue(recoverableInt.isExecuted());
    assertTrue(execFlag.get());
    assertTrue(recoverableInt.getExecutionTimestamp().isPresent());
    assertTrue(recoverableInt.isSuccessful());
    assertTrue(recoverableInt.getRecoveryCause().isPresent());
    assertFalse(recoverableInt.getFailureCause().isPresent());
    assertEquals(IllegalStateException.class, recoverableInt.getRecoveryCause().get().getClass());
    assertEquals(fallbackValue, recoverableInt.getResult());
  }

  @Test
  void testRecoveryWithStaticValue() {
    final int fallbackValue = 5;

    Recoverable<Integer> recoverableInt =
        Recoverable.<Integer>of(() -> {
          throw new IllegalStateException("Cannot supply value");
        })
            .recoverIf(IllegalStateException.class, fallbackValue)
            .go();

    assertTrue(recoverableInt.isExecuted());
    assertTrue(recoverableInt.getExecutionTimestamp().isPresent());
    assertTrue(recoverableInt.isSuccessful());
    assertTrue(recoverableInt.getRecoveryCause().isPresent());
    assertFalse(recoverableInt.getFailureCause().isPresent());
    assertEquals(IllegalStateException.class, recoverableInt.getRecoveryCause().get().getClass());
    assertEquals(fallbackValue, recoverableInt.getResult());
  }

  @Test
  void testNoFallbacks() {
    Recoverable recoverable =
        Recoverable.of(() -> {
          throw new IllegalStateException("Cannot supply value");
        });

    assertThrows(IllegalStateException.class,
        () -> recoverable.go());

    assertTrue(recoverable.isExecuted());
    assertTrue(recoverable.getExecutionTimestamp().isPresent());
    assertFalse(recoverable.isSuccessful());
    assertFalse(recoverable.getRecoveryCause().isPresent());
    assertTrue(recoverable.getFailureCause().isPresent());
    assertEquals(IllegalStateException.class, recoverable.getFailureCause().get().getClass());
  }

  @Test
  void testFailureWithFinally() {
    AtomicBoolean execFlag = new AtomicBoolean(false);

    Recoverable recoverable =
        Recoverable.of(() -> {
          throw new IllegalStateException("Cannot supply value");
        }).finallyDo(() -> execFlag.set(true));

    assertThrows(IllegalStateException.class,
        () -> recoverable.go());

    assertTrue(recoverable.isExecuted());
    assertTrue(execFlag.get());
    assertTrue(recoverable.getExecutionTimestamp().isPresent());
    assertFalse(recoverable.isSuccessful());
    assertFalse(recoverable.getRecoveryCause().isPresent());
    assertTrue(recoverable.getFailureCause().isPresent());
    assertEquals(IllegalStateException.class, recoverable.getFailureCause().get().getClass());
  }

}