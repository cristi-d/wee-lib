package net.techtrix.wee.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Try<T> {
  private Supplier<T> supplier;
  private List<Catcher> catchers = new ArrayList<>();
  private FinallyBlock finallyBlock;
  private boolean executed;
  private boolean successful;

  private Try(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public static <T> Try<T> of(Supplier<T> supplier) {
    return new Try<>(supplier);
  }


  public <E extends Exception> Try<T> fallback(Class<E> exceptionClass, Supplier<T> fallback) {
    catchers.add(new FallbackCatcher(exceptionClass, fallback));
    return this;
  }

  public <E extends Exception> Try<T> abort(Class<E> exceptionClass, Consumer<Exception> consumer) {
    this.catchers.add(new AbortingCatcher(exceptionClass, consumer));
    return this;
  }

  public Try<T> andFinally(FinallyBlock finallyBlock) {
    this.finallyBlock = finallyBlock;

    return this;
  }

  public T go() {
    try {
      this.executed = true;
      T result = this.supplier.get();
      this.successful = true;

      return result;
    } catch (Exception e) {
      Optional<Catcher> optFirstCatcher =
          catchers.stream()
              .filter(catcher -> catcher.getExceptionClass().isAssignableFrom(e.getClass()))
              .findFirst();

      if (optFirstCatcher.isPresent()) {
        Catcher firstCatcher = optFirstCatcher.get();

        if (firstCatcher instanceof FallbackCatcher) {
          FallbackCatcher<T> fallbackCatcher = (FallbackCatcher<T>) firstCatcher;

          return fallbackCatcher.fallbackSupplier.get();
        } else {
          AbortingCatcher<T> abortingCatcher = (AbortingCatcher<T>) firstCatcher;

          abortingCatcher.exceptionConsumer.accept(e);
          throw e;
        }
      }

      throw e;
    } finally {
      if (finallyBlock != null) {
        finallyBlock.execute();
      }
    }
  }

  public boolean isExecuted() {
    return executed;
  }

  public boolean isSuccessful() {
    return successful;
  }

  abstract class Catcher<T> {
    Class<? extends Exception> exceptionClass;

    Catcher(Class<? extends Exception> exceptionClass) {
      this.exceptionClass = exceptionClass;
    }

    Class<? extends Exception> getExceptionClass() {
      return exceptionClass;
    }
  }

  class FallbackCatcher<T> extends Catcher<T> {
    Supplier<T> fallbackSupplier;

    FallbackCatcher(Class<? extends Exception> exceptionClass, Supplier<T> fallbackSupplier) {
      super(exceptionClass);
      this.fallbackSupplier = fallbackSupplier;
    }
  }

  class AbortingCatcher<T> extends Catcher<T> {
    Consumer<Exception> exceptionConsumer;

    AbortingCatcher(Class<? extends Exception> exceptionClass, Consumer<Exception> exceptionConsumer) {
      super(exceptionClass);
      this.exceptionConsumer = exceptionConsumer;
    }
  }

  interface FinallyBlock {
    void execute();
  }


}
