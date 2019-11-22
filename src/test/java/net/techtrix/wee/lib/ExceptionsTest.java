package net.techtrix.wee.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

  @Test
  public void testBuilderFacade() {
    IllegalStateException result =
        Exceptions
            .builder(IllegalStateException.class)
            .withMessage("Some {} message", "horrific")
            .build();

    assertNull(result.getCause());

    String expectedMsg = "Some horrific message";
    assertEquals(expectedMsg, result.getMessage());
  }
}