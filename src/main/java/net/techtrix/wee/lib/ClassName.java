package net.techtrix.wee.lib;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class ClassName implements CharSequence {
  public static final String NULL = "null";
  final String className;

  private ClassName(Object obj) {
    this.className =
        Optional.ofNullable(obj)
            .map(it -> {
              if (it instanceof Class) {
                return ((Class) it).getSimpleName();
              }

              return it.getClass().getSimpleName();
            })
            .orElse(NULL);

  }

  public static ClassName of(Object obj) {
    return new ClassName(obj);
  }

  @Override
  public int length() {
    return className.length();
  }

  @Override
  public char charAt(int i) {
    return className.charAt(i);
  }

  @Override
  public CharSequence subSequence(int i, int i1) {
    return className.subSequence(i, i1);
  }

  @Override
  public IntStream chars() {
    return className.chars();
  }

  @Override
  public IntStream codePoints() {
    return className.codePoints();
  }

  @Override
  public String toString() {
    return className;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ClassName) {
      return Objects.equals(this.className, ((ClassName) other).className);
    }

    if (other instanceof CharSequence) {
      return Objects.equals(other, className);
    }


    return other != null && other.equals(this);
  }
}
