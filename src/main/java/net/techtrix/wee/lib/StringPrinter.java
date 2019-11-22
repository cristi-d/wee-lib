package net.techtrix.wee.lib;

import java.io.PrintStream;

public class StringPrinter {
  private final PrintStream printStream;

  StringPrinter(PrintStream printStream) {
    this.printStream = printStream;
  }

  public void println(String format, Object ... params) {
    String formatted = Strings.create(format, params);

    printStream.println(formatted);
  }

  public void print(String format, Object ... params) {
    String formatted = Strings.create(format, params);

    printStream.print(formatted);
  }
}
