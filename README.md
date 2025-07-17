# Timestone

[![License](https://img.shields.io/github/license/predatorray/timestone)][1]
![Maven Central](https://img.shields.io/maven-central/v/io.github.predatorray/timestone-api)
![GitHub release version](https://img.shields.io/github/v/release/predatorray/timestone)
![Build Status](https://img.shields.io/github/actions/workflow/status/predatorray/timestone/ci.yml?branch=main)

Timestone is a Java library for abstracting time sources, enabling deterministic and testable time-dependent logic.
It provides interfaces and implementations for system time and mutable time, compatible with Java's `Clock`.

## Features

- `Time` interface for accessing current time and sleeping.
- `SystemTime` for real system clock.
- `MutableTime` for controllable time in tests.
- Seamless integration with Java's `Clock`.

## Usage

Import the Timestone API in your project.

If you're using Maven, add the following dependency to your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>io.github.predatorray</groupId>
        <artifactId>timestone-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>io.github.predatorray</groupId>
        <artifactId>timestone-test</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

If you're using Gradle, add the following to your `build.gradle`:
```groovy
dependencies {
    implementation 'io.github.predatorray:timestone-api:1.0.0'
    testImplementation 'io.github.predatorray:timestone-test:1.0.0'
}
```

### For Production Code

```java
// Use system time
import io.github.predatorray.timestone.Time;
import io.github.predatorray.timestone.SystemTime;

Time time = Time.SYSTEM;

long nowMillis = time.millis();
time.sleep(1000);
```

### For Unit Tests

```java
// Use mutable time in tests
import io.github.predatorray.timestone.test.MutableTime;

MutableTime testTime = new MutableTime();

testTime.advance(Duration.ofSeconds(5));
```

## License

This project is licensed under the [MIT License][1].

[1]: ./LICENSE

