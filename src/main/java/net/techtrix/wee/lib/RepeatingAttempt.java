package net.techtrix.wee.lib;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RepeatingAttempt<T> {
  public static final Duration defaultDelay = Duration.ofMillis(200);
  public static final long defaultMaxAttempts = 100;

  private Supplier<T> supplier;
  private Predicate<T> until;
  private Duration delay = defaultDelay;
  private long maxAttempts = defaultMaxAttempts;

  private boolean started;
  private boolean successful;
  private boolean finished;
  private Optional<T> result;
  private long nrAttempts;

  private RepeatingAttempt() {
  }

  public static <T> RepeatingAttempt<T> of(Supplier<T> supplier) {
    Objects.requireNonNull(supplier, "Supplier cannot be null");

    RepeatingAttempt<T> poll = new RepeatingAttempt<>();
    poll.supplier = supplier;

    return poll;
  }

  public RepeatingAttempt<T> go() {
    Objects.requireNonNull(until, "Completion test must not be null");
    this.started = true;

    nrAttempts = 0;

    while (nrAttempts < this.maxAttempts) {

      T result = supplier.get();
      nrAttempts++;

      if (until.test(result)) {
        this.finished = true;
        this.successful = true;
        this.result = Optional.ofNullable(result);

        return this;
      }

      try {
        Thread.sleep(delay.toMillis());
      } catch (InterruptedException e) {
      }

    }

    this.finished = true;
    this.successful = false;
    this.result = Optional.empty();

    return this;
  }

  public boolean isStarted() {
    return started;
  }

  public boolean isSuccessful() {
    return successful;
  }

  public boolean isFinished() {
    return finished;
  }

  public Optional<T> getResult() {
    return result;
  }

  public long getNrAttempts() {
    return nrAttempts;
  }

  public RepeatingAttempt<T> until(Predicate<T> completionTest) {
    Objects.requireNonNull(completionTest, "Completion test cannot be null");
    this.until = completionTest;

    return this;
  }

  public RepeatingAttempt<T> fixedDelay(Duration delay) {
    Objects.requireNonNull(delay, "Delay cannot be null");
    this.delay = delay;

    return this;
  }

  public RepeatingAttempt<T> abortingAfter(long maxAttempts) {
    this.maxAttempts = maxAttempts;

    return this;
  }
}
