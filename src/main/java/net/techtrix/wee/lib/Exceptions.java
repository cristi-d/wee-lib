package net.techtrix.wee.lib;

public class Exceptions {

  private Exceptions() {
  }

  public static <T extends Exception> ExceptionBuilder<T> builder(Class<T> exceptionClass) {
    return ExceptionBuilder.of(exceptionClass);
  }
}
