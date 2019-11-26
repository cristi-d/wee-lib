package net.techtrix.wee.lib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

class ExceptionBuilder<T extends Exception> {
  private Throwable cause;
  private String message;
  private Class<T> exceptionClass;

  private ExceptionBuilder() {
  }

  static <T extends Exception> ExceptionBuilder<T> of(Class<T> exceptionClass) {
    ExceptionBuilder<T> builder = new ExceptionBuilder<>();
    builder.exceptionClass = exceptionClass;

    return builder;
  }

  public ExceptionBuilder<T> withCause(Throwable cause) {
    this.cause = cause;

    return this;
  }

  public ExceptionBuilder<T> withMessage(String message, Object parameters) {
    String formattedMessage = Strings.create(message, parameters);
    this.message = formattedMessage;

    return this;
  }

  public T build() {
    final String exceptionClassName = exceptionClass.getSimpleName();
    final Class<?>[] constructorParamTypes;
    final Object[] constructorParams;
    final Constructor<T> constructor;

    if (message == null && cause == null) {
      constructorParamTypes = new Class[0];
      constructorParams = new Object[0];
    } else {
      if (cause == null) {
        constructorParamTypes = new Class[]{String.class};
        constructorParams = new Object[]{message};
      } else {
        constructorParamTypes = new Class[]{String.class, Throwable.class};
        constructorParams = new Object[]{message, cause};
      }
    }

    try {
      return exceptionClass.getConstructor(constructorParamTypes).newInstance(constructorParams);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new CreationException(e, exceptionClass, message, cause);
    }
  }

  public void buildAndThrow() throws T {
    T ex = build();

    throw ex;
  }

  public static class CreationException extends RuntimeException {
    private Throwable creationException;
    private Throwable causeParam;
    private String messageParam;
    private Class<? extends Exception> exceptionClassParam;

    CreationException(
        Throwable creationException,
        Class<? extends Exception> exceptionClassParam,
        String messageParam,
        Throwable causeParam) {
      super(creationException);
      this.creationException = creationException;
      this.exceptionClassParam = exceptionClassParam;
      this.messageParam = messageParam;
      this.causeParam = causeParam;
    }

    @Override
    public String getMessage() {
      return Strings.create(
          "Encountered {} while trying to instantiate exception of class {} with message {} and cause {}{}.",
          ClassName.of(this.creationException),
          ClassName.of(exceptionClassParam),
          messageParam,
          ClassName.of(causeParam),
          Optional
              .ofNullable(causeParam)
              .map(Throwable::getMessage)
              .map(it -> String.format(" (%s)", it))
              .orElse(""));
    }
  }
}
