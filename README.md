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

Requires Java 21 or later. The build is tested on Java 21, 23 and 25.

```groovy
implementation 'org.kleinb:jfun-core:0.0.6'
```

jFun is meant to be a library without transitive dependencies: adding it to your build never
pulls in third-party libraries.

## Modules

Only `jfun-core` is published to Maven Central at the moment.

| Module          | Description                                                                   | Published |
|-----------------|-------------------------------------------------------------------------------|-----------|
| `jfun-core`     | Immutable functional data types and function types for modern Java.           | yes       |
| `jfun-optics`   | Optics for reading and updating nested immutable data. Builds on `jfun-core`. | not yet   |
| `jfun-examples` | Runnable examples that show the library in use.                               | no        |

## Build

```
./gradlew build
```

## License

[Apache-2.0](LICENSE.txt)
