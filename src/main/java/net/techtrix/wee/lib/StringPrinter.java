package net.techtrix.wee.lib;

import java.io.PrintStream;

public class StringPrinter {
  private final PrintStream printStream;

  StringPrinter(PrintStream printStream) {
    this.printStream = printStream;
  }

  public StringPrinter println(String format, Object ... params) {
    String formatted = Strings.create(format, params);

    printStream.println(formatted);

    return this;
  }

  public StringPrinter print(String format, Object ... params) {
    String formatted = Strings.create(format, params);

    printStream.print(formatted);

    return this;
  }
}
