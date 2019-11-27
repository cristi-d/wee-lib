package io.techtrix.wee.lib;

import java.io.PrintStream;

public class Strings {
  private static final Printers printers = new Printers();

  private Strings() {
  }

  public static String create(String format, Object... parameters) {
    return StringCreator.create(format, parameters);
  }

  public static Printers print() {
    return printers;
  }

  /**
   * Checks whether a String is null or empty
   *
   * @param str
   * @return
   */
  public static boolean isEmpty(String str) {
    return str == null || "".equals(str);
  }

  public static boolean notEmpty(String str) {
    return !isEmpty(str);
  }

  static class Printers {
    private static final StringPrinter stdoutPrinter = new StringPrinter(System.out);
    private static final StringPrinter stderrPrinter = new StringPrinter(System.err);

    public StringPrinter stdout() {
      return stdoutPrinter;
    }

    public StringPrinter stderr() {
      return stderrPrinter;
    }

    public StringPrinter to(PrintStream printStream) {
      return new StringPrinter(printStream);
    }

  }
}
