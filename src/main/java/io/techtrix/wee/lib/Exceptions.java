package io.techtrix.wee.lib;

public class Exceptions {

  private Exceptions() {
  }

  public static <T extends Exception> ExceptionBuilder<T> create(Class<T> exceptionClass) {
    return ExceptionBuilder.of(exceptionClass);
  }
}
