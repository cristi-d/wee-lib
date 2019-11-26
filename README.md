# The _wee_ library

No extra dependencies, just a few helper objects for your daily Java needs

## Lazy
Helper object for deferring expensive and probably optional computations

```
Lazy<Rock> rock = Lazy.of(() -> pushTheRockUpTheMountain());

if (sysyphus.isntTired()) {
  rock.get();
}
```

## Recoverable
Useful for operations for which we can provide fallbacks in case of failures

```
AtomicBoolean executionFlag = new AtomicBoolean(false);

Recoverable<String> serviceHostnameFetcher =  
    Recoverable
      .of(() -> pingAndReturnFirstAvailableNode())
      .recoverIf(SocketException.class, () -> getLocalHostname())
      .finallyDo(() -> executionFlag.set(true))
      .go();

assertTrue(serviceHostnameFetcher.isExecuted());
assertTrue(executionFlag.get());
assertTrue(serviceHostnameFetcher.getExecutionTimestamp().isPresent());
assertTrue(serviceHostnameFetcher.isSuccessful());
assertTrue(serviceHostnameFetcher.getRecoveryCause().isPresent());
assertFalse(serviceHostnameFetcher.getFailureCause().isPresent());
assertEquals(SocketException.class, serviceHostnameFetcher.getRecoveryCause().get().getClass());
assertEquals(getLocalHostname(), serviceHostnameFetcher.getResult());

```

## RepeatingAttempt
Useful for polling or retrying
```

RepeatingAttempt<Integer> progressCheck =
    RepeatingAttempt
      .of(() -> {
        int progress = pollProgress();
    
        return progress; 
      })
      .until((progress) -> progress == 100) //when to stop attempting
      .fixedDelay(300) //300 millis between attempts
      .abortingAfter(10) //aborting after 10 attempts
      .go();

assertTrue(progressCheck.isStarted());
assertTrue(progressCheck.isFinished());

//In case we did reached the result we were attempting to get
assertTrue(progressCheck.isSuccessful()); 
assertTrue(progressCheck.getResult().isPresent());
assertEquals(100, progressCheck.getResult().get());
```

## Exceptions
Facade for building exceptions with templated messages
```
//Create an Exception object
IllegalStateException forSafeKeeping = 
  Exceptions
    .builder(IllegalStateException.class)
    .withMessage("All of the sudden, without any apparent cause we decided to create an exception. Epoch was {} millis ago", System.currentTimeMillis())
    .build();

//Or create and throw it
Exception cause = new UnsupportedOperationException("Oops...");
String panicDegree = Math.random() > 0.5 ? " very " : " ";
Exceptions
  .builder(IllegalStateException.class)
  .withCause(cause)
  .withMessage("Encountered a{}nasty exception", panicDegree)
  .buildAndThrow();
```

## Strings
Facade for creating and printing templated strings.
```
String adjective = "wonderful";
String punctuation = Math.random() > 0.5 ? "!" : ".";
String template = "This is a {} example {}";

String result = Strings.create(template, adjective, punctuation);
assertEquals("This is a wonderful example " + punctuation, result);

Strings.print().stdout().println(template, adjective, punctuation);

PrintStream somePrintStream = createAnExcellentPrintStream();
Strings.print().to(somePrintStream).println(template, adjective, punctuation);

//Also, the StringPrinter's methods are chainable
Strings.print().stdout()
  .println("Right now, at {}", System.currentTimeMillis())
  .println("We are pretty bored with the whole situation")
  .println("One can only hope that something {}ly entertaining will happen", adjective);
```

Also for checking if a string is empty/null or not.
```
assertTrue(Strings.isEmpty(null));
assertTrue(Strings.isEmpty(""));
assertFalse(Strings.isEmpty(" ");

assertFalse(Strings.isNotEmpty(null));
assertFalse(Strings.isNotEmpty(""))
assertTrue(Strings.isNotEmpty(" "))
```

## Classes

Facade for everything related to classes. 
At the moment, can help you only with fetching a class's _simpleName_ wrapped in the ClassName object which implements the CharSequence interface (so it's ready to print or compare)

```
Object someObject = UUID.randomUUID();
ClassName className = Classes.name(someObject);
assertTrue("UUID".equals(className));
```
