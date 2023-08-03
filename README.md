# LittlePay Code Test

## Prerequisites

* Java 17

Example

```shell
asdf install java openjdk-17.0.2
```

## Usage

```shell
./gradlew(.bat) run --args="<relative path to taps.csv> <realtive path to trips.cvs>"
```

Example

```shell
./gradlew run --args="./src/test/resources/demo/taps.csv ./src/test/resources/demo/trips.csv"
```

## Tests

```shell
./gradlew(.bat) test
```

Example

```shell
./gradlew test
```
