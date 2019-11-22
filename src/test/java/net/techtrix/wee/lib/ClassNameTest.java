package net.techtrix.wee.lib;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassNameTest {

  @Test
  void ofNullObject() {
    ClassName result = ClassName.of(null);

    assertEquals(ClassName.NULL, result.toString());
  }

  @Test
  void ofClassObject() {
    ClassName result = ClassName.of(IllegalStateException.class);

    assertEquals(IllegalStateException.class.getSimpleName(), result.toString());
  }

  @Test
  void ofInstance() {
    IllegalStateException ex = new IllegalStateException();
    ClassName result = ClassName.of(ex);

    assertEquals(IllegalStateException.class.getSimpleName(), result.toString());
  }

  @Test
  void testEqualsToCharSequenceObjectType() {
    String expectedClassNameString = IllegalStateException.class.getSimpleName();
    ClassName result = ClassName.of(IllegalStateException.class);

    assertTrue(result.equals(expectedClassNameString));
  }

  @Test
  void testEqualsToNonCharSequenceObjectType() {
    ClassName sut = ClassName.of(IllegalStateException.class);
    UUID toCompare = UUID.randomUUID();

    assertFalse(sut.equals(toCompare));
  }

  @Test
  void testEqualsToClassNameObjectType() {
    ClassName first = ClassName.of(IllegalStateException.class);
    ClassName second = ClassName.of(IllegalStateException.class);

    assertTrue(first.equals(second));
  }

  @Test
  void testToString() {
    String expectedClassNameString = IllegalStateException.class.getSimpleName();
    ClassName result = ClassName.of(IllegalStateException.class);

    assertEquals(expectedClassNameString, result.toString());
  }

  @Test
  void testCharSequenceImplementation() {
    String expectedClassNameString = IllegalStateException.class.getSimpleName();
    ClassName result = ClassName.of(IllegalStateException.class);

    assertEquals(expectedClassNameString.length(), result.length());
    assertEquals(expectedClassNameString.charAt(0), result.charAt(0));
    assertEquals(expectedClassNameString.subSequence(0, 2), result.subSequence(0, 2));
    assertIntStreamsEqual(expectedClassNameString.chars(), result.chars());
    assertIntStreamsEqual(expectedClassNameString.codePoints(), result.codePoints());
    ;
  }

  void assertIntStreamsEqual(IntStream first, IntStream second) {
    List<Integer> firstValues = first.boxed().collect(Collectors.toList());
    List<Integer> secondValues = second.boxed().collect(Collectors.toList());

    assertIterableEquals(firstValues, secondValues);
  }
}