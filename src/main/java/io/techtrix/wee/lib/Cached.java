package io.techtrix.wee.lib;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class Cached<T> {
  public static final Duration defaultRetention = Duration.ofHours(1);

  private Supplier<T> supplier;
  private Duration retentionPeriod = defaultRetention;
  private Instant computedAt;

  private T value;

  private Cached() {

  }

  public static <T> Cached<T> of(Supplier<T> supplier) {
    Cached<T> cached = new Cached<>();
    cached.supplier = supplier;

    return cached;
  }

  public Cached<T> retainingFor(Duration period) {
    this.retentionPeriod = period;

    return this;
  }

  public T get() {
    if (shouldComputeValue()) {
      synchronized (this) {
        //Double check that no other thread got to set the value
        if (shouldComputeValue()) {
          value = supplier.get();
          computedAt = Instant.now();
        }
      }
    }

    return value;
  }

  private boolean shouldComputeValue() {
    return value == null || valueHasExpired();
  }

  private boolean valueHasExpired() {
    return computedAt == null || Duration.between(computedAt, Instant.now()).compareTo(retentionPeriod) >= 0;
  }
}
