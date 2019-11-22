package net.techtrix.wee.lib;

import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StringPrinterTest {

  public static final int ONCE = 1;

  @Test
  void println() {
    PrintStream spiedPrintStream = Mockito.spy(System.out);
    StringPrinter sut = new StringPrinter(spiedPrintStream);

    String expected = "Some thing";
    sut.println(expected);

    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(spiedPrintStream, times(ONCE)).println(argumentCaptor.capture());

    assertEquals(expected, argumentCaptor.getValue());

  }

  @Test
  void print() {
    PrintStream spiedPrintStream = Mockito.spy(System.out);
    StringPrinter sut = new StringPrinter(spiedPrintStream);

    String expected = "Some thing";
    sut.print(expected);

    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(spiedPrintStream, times(ONCE)).print(argumentCaptor.capture());

    assertEquals(expected, argumentCaptor.getValue());
  }
}