package net.techtrix.wee.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TryTest {

  private static final String FALLBACK_2 = "Fallback_2";
  private static final String FALLBACK = "Fallback";
  private static final String RESULT = "result";

  @Test
  void testFallbackCatcher() {
    String result =
        Try
            .of(() -> {
              if (Math.random() < 1.1) {
                throw new IllegalStateException("oh no");
              }
              return "Something";
            })
            .fallback(IllegalStateException.class, () -> FALLBACK)
            .fallback(Exception.class, () -> FALLBACK_2)
            .go();

    assertEquals(FALLBACK, result);
  }

  @Test
  void testAbortingCatcherWrappingException() {
    assertThrows(IllegalArgumentException.class,
        () ->
            Try.of(() -> {
              if (Math.random() < 1.1) {
                throw new IllegalStateException("oh no");
              }
              return "Something";
            }).abort(IllegalStateException.class, ex -> {
              throw new IllegalArgumentException(ex);
            }).go());
  }

  @Test
  void testAbortingCatcher() {
    assertThrows(IllegalStateException.class,
        () ->
            Try.of(() -> {
              if (Math.random() < 1.1) {
                throw new IllegalStateException("oh no");
              }
              return "Something";
            }).abort(IllegalStateException.class, ex -> {
              Strings.print().stdout().println("Well... {}", Classes.name(ex));
            }).go());
  }

  @Test
  void testFinallyBlock() {
    boolean[] gotHere = new boolean[1];
    gotHere[0] = false;

    assertThrows(IllegalStateException.class, () ->
        Try
            .of(() -> {
              if (Math.random() < 1.1) {
                throw new IllegalStateException("oh no");
              }
              return "Something";
            })
            .andFinally(() -> gotHere[0] = true)
            .go());

    assertTrue(gotHere[0]);
  }

  @Test
  void testSuccessfulExecution() {
    boolean[] gotHere = new boolean[1];
    gotHere[0] = false;


    Try<String> stringTry =
        Try.of(() -> RESULT)
        .andFinally(() -> gotHere[0] = true);

    String result = stringTry.go();

    assertEquals(RESULT, result);
    assertTrue(gotHere[0]);
    assertTrue(stringTry.isExecuted());
    assertTrue(stringTry.isSuccessful());
  }
}