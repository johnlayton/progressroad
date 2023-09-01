# LittlePay Code Test

## Prerequisites

* Java 17

Example

```shell
asdf install java openjdk-17.0.2
```

## Usage

```shell
./gradlew(.bat) run --args="<relative path to taps.csv> <relative path to trips.cvs>"
```

Example

```shell
./gradlew run --args="./src/test/resources/org/progressroad/app/taps.csv ./src/test/resources/org/progressroad/app/trips.csv"
```

## Tests

```shell
./gradlew(.bat) test
```

Example

```shell
./gradlew test
```
