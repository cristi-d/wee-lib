package net.techtrix.wee.lib;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Recoverable<T> {
  private Supplier<T> supplier;
  private List<Class<? extends Exception>> recoverableExceptionClasses = new ArrayList<>();
  private Map<Class<? extends Exception>, Supplier<T>> fallbacks = new HashMap<>();
  private T result;

  private Optional<Instant> executionTimestamp;
  private Optional<Exception> failureCause = Optional.empty();
  private Optional<Exception> recoveredFrom = Optional.empty();
  private Runnable finallyBlock;

  private Recoverable() {
  }

  public static <T> Recoverable<T> of(Supplier<T> supplier) {

    Recoverable<T> recoverable = new Recoverable<>();
    recoverable.supplier = supplier;

    return recoverable;
  }

  public Recoverable<T> go() {
    try {
      this.executionTimestamp = Optional.of(Instant.now());

      result = supplier.get();

      return this;
    } catch (Exception e) {
      Class<? extends Exception> exceptionClass = e.getClass();

      Optional<Class<? extends Exception>> firstMatchingFallbackExceptionClass =
          recoverableExceptionClasses.stream()
              .filter(fallbackExClass -> fallbackExClass.isAssignableFrom(exceptionClass))
              .findFirst();

      if (firstMatchingFallbackExceptionClass.isPresent()) {
        Supplier<T> fallbackValueSupplier = fallbacks.get(firstMatchingFallbackExceptionClass.get());

        result = fallbackValueSupplier.get();

        this.recoveredFrom = Optional.of(e);
        return this;
      }

      this.failureCause = Optional.of(e);
      throw e;
    } finally {
      if (finallyBlock != null) {
        finallyBlock.run();
      }
    }
  }

  public T getResult() {
    return result;
  }

  public boolean isExecuted() {
    return executionTimestamp.isPresent();
  }

  public Optional<Instant> getExecutionTimestamp() {
    return executionTimestamp;
  }

  public boolean isSuccessful() {
    return !failureCause.isPresent();
  }

  public Optional<Exception> getRecoveryCause() {
    return recoveredFrom;
  }

  public Optional<Exception> getFailureCause() {
    return failureCause;
  }

  public Recoverable<T> recoverIf(Class<? extends Exception> exceptionClass, T fallbackValue) {
    return recoverIf(exceptionClass, () -> fallbackValue);
  }

  public Recoverable<T> recoverIf(Class<? extends Exception> exceptionClass, Supplier<T> fallbackValueSupplier) {
    recoverableExceptionClasses.add(exceptionClass);
    fallbacks.put(exceptionClass, fallbackValueSupplier);

    return this;
  }

  public Recoverable<T> finallyDo(Runnable finallyBlock) {
    this.finallyBlock = finallyBlock;

    return this;
  }

}
