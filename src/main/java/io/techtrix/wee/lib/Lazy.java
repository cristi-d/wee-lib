package io.techtrix.wee.lib;

import java.util.function.Supplier;

public class Lazy<T> {
  private final Supplier<T> supplier;
  private T instance = null;

  private Lazy(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public static <T> Lazy<T> of(Supplier<T> supplier) {
    return new Lazy<>(supplier);
  }

  public T get() {
    if (this.instance == null) {
      synchronized (this) {
        //Double check that no other thread got a chance to set it
        if (this.instance == null) {
          this.instance = supplier.get();
        }
      }
    }
    return this.instance;
  }
}
