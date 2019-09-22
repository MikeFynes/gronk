# Gronk - a formatter
A simple API that takes an input of days mapped to a list of times and returns a formatted string for the week.

## Setup

- Run the below command to build and run locally

### Linux / Mac
```shell script
sh gradlew bootRun
```

### Windows
```shell script
gradlew.bat bootRun
```

This will build the `kronk` module, and deploy the `fronk` Spring Boot application locally.

## Importing to Intellij IDEA

Import the parent `build.gradle.kts` file under `gronk` folder, this will setup the project as intended.

## Tests
The critical tests are in the `kronk` module, the `fronk` Spring Boot setup is purely used as an API framework

To run the tests run the below gradle command
```shell script
sh gradlew test
```

## Structure
- Module approach means `kronk` has no Spring boot dependencies, can easily switch to other framework
- Using Arrow library to get simpler composition tricks, makes life easier

## Improvements
- It doesnt make much sense to format dates/times in a backend service
- Autowiring could be used in Spring boot side
- Validation of output, currently it is not checking if all days have a value
- Validation of input, assuming all days have a value or empty list (could use monads for Railway Oriented Programming style here)

