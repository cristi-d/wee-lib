package net.techtrix.wee.lib;

import java.util.function.Supplier;

public class Exceptions {

  private Exceptions() {
  }

  public static <T extends Exception> ExceptionBuilder<T> builder(Class<T> exceptionClass) {
    return ExceptionBuilder.of(exceptionClass);
  }

  public static <T> Try<T> attempt(Supplier<T> supplier) {
    return Try.of(supplier);
  }
}
