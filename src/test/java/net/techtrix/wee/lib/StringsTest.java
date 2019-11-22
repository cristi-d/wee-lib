package net.techtrix.wee.lib;

import com.sun.org.apache.xpath.internal.Arg;
import java.io.PrintStream;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StringsTest {


  public static final int ONCE = 1;

  @Test
  void testCreateNullFormat() {
    String result = Strings.create(null);

    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  void testCreateWithNullParam() {
    String format = "Some days you just give {} fudges";
    String result = Strings.create(format, new Object[]{null});

    String expected = "Some days you just give null fudges";
    assertEquals(expected, result);
  }

  @Test
  void testCreateSimpleTemplate() {
    String format = "This is a {} example";
    String parameter = "wonderful";
    String expected = "This is a wonderful example";

    String result = Strings.create(format, parameter);

    assertEquals(expected, result);
  }

  @Test
  void testCreateComplexTemplate() {
    String format = "This is a {} example and it's very {}";
    String[] parameters = {"wonderful", "complex"};
    String expected = "This is a wonderful example and it's very complex";

    String result = Strings.create(format, parameters);

    assertEquals(expected, result);
  }

  @Test
  void testCreateComplexTemplateWithVariousParameterTypes() {
    String format = "This is the {}st example that poses {} {} challenges and it has the id {}";
    Object[] parameters = {1, true, "exceptional", UUID.randomUUID()};
    String expected =
        "This is the 1st example that poses true exceptional challenges and it has the id " + parameters[3];

    String result = Strings.create(format, parameters);

    assertEquals(expected, result);
  }

  @Test
  void testCreateWithoutParameters() {
    String format = "This is a wonderful example";

    String result = Strings.create(format);

    assertEquals(format, result);
  }

  @Test
  void isEmpty() {
    assertTrue(Strings.isEmpty(""));
    assertTrue(Strings.isEmpty(null));
    assertFalse(Strings.isEmpty(" "));
    assertFalse(Strings.isEmpty("not empty"));
  }

  @Test
  void isNotEmpty() {
    assertFalse(Strings.notEmpty(""));
    assertFalse(Strings.notEmpty(null));
    assertTrue(Strings.notEmpty(" "));
    assertTrue(Strings.notEmpty("not empty"));
  }

  @Test
  void testPrinterFacade() {
    String expected = "Something wonderful";
    PrintStream spiedPrintStream = Mockito.spy(System.out);


    Strings.print().to(spiedPrintStream).println(expected);

    ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
    verify(spiedPrintStream, times(ONCE)).println(argCaptor.capture());

    assertEquals(expected, argCaptor.getValue());
  }

  @Test
  void testPrinterFacadeDefaultPrinters() {
    String adjective = "wonderful";
    StringPrinter stdoutPrinter = Strings.print().stdout();
    assertNotNull(stdoutPrinter);
    stdoutPrinter.println("something {}", adjective);

    StringPrinter stderrPrinter = Strings.print().stderr();
    assertNotNull(stderrPrinter);
    stderrPrinter.println("something {}", adjective);
  }
}