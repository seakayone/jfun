# jFun

Functional data structures for modern Java: `Option`, `Either`, `Try`, `Validation`,
`NonEmptyList` and `Tuple`. Make functional programming on Java fun again.

```java
Try<Integer> port = Try.of(() -> Integer.parseInt(text));

String message =
    switch (port) {
      case Try.Success<Integer>(var value) -> "Listening on " + value;
      case Try.Failure<Integer>(var error) -> "Invalid port: " + error.getMessage();
    };
```

## Usage

Requires Java 21 or later.

```groovy
implementation 'org.kleinb:jfun-core:0.0.5'
```

## Build

```
./gradlew build
```

## License

[Apache-2.0](LICENSE.txt)
