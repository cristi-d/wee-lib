package net.techtrix.wee.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionBuilderTest {

  public static final String BIG = "big";

  @Test
  void testBuildWithMessageAndCause() {
    UnsupportedOperationException cause = new UnsupportedOperationException("Well ...");

    IllegalStateException result =
        ExceptionBuilder
            .of(IllegalStateException.class)
            .withCause(cause)
            .withMessage("This is a {} problem", BIG)
            .build();

    assertEquals(cause, result.getCause());

    String expectedMsg = "This is a big problem";
    assertEquals(expectedMsg, result.getMessage());
  }

  @Test
  void testBuildWithMessage() {
    IllegalStateException result =
        ExceptionBuilder
            .of(IllegalStateException.class)
            .withMessage("This is a {} problem", BIG)
            .build();

    String expectedMsg = "This is a big problem";
    assertEquals(expectedMsg, result.getMessage());

    assertNull(result.getCause());
  }

  @Test
  void testBuildWithCause() {
    UnsupportedOperationException cause = new UnsupportedOperationException("Well ...");

    IllegalStateException result =
        ExceptionBuilder
            .of(IllegalStateException.class)
            .withCause(cause)
            .build();

    assertEquals(cause, result.getCause());
    assertNull(result.getMessage());
  }

  @Test
  void testBuildWithNone() {

    IllegalStateException result =
        ExceptionBuilder
            .of(IllegalStateException.class)
            .build();

    assertNull(result.getCause());
    assertNull(result.getMessage());
  }

  @Test
  void testBuilderWithNoConstructorAvailable() {
    class PrivateException extends Exception {
      private PrivateException() {

      }
    }

    ExceptionBuilder.CreationException exception =
        assertThrows(ExceptionBuilder.CreationException.class,
            () ->
                ExceptionBuilder
                    .of(PrivateException.class)
                    .build());

    System.out.println(exception.getMessage());
  }

  @Test
  void testBuildAndThrowWithMessageAndCause() {
    UnsupportedOperationException cause = new UnsupportedOperationException("Well ...");

    assertThrows(IllegalStateException.class,
        () ->
            ExceptionBuilder
                .of(IllegalStateException.class)
                .withCause(cause)
                .withMessage("This is a {} problem", BIG)
                .buildAndThrow());
  }

}