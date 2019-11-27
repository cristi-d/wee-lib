package io.techtrix.wee.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassesTest {

  @Test
  void testClassNameFacade() {
    ClassName result = Classes.name(IllegalStateException.class);

    assertEquals(IllegalStateException.class.getSimpleName(), result.toString());
  }
}