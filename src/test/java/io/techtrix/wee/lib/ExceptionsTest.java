package io.techtrix.wee.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

  @Test
  void testBuilderFacade() {
    IllegalStateException result =
        Exceptions
            .create(IllegalStateException.class)
            .withMessage("Some {} message", "horrific")
            .build();

    assertNull(result.getCause());

    String expectedMsg = "Some horrific message";
    assertEquals(expectedMsg, result.getMessage());
  }
}