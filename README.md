# Gronk - a formatter
A simple API that takes an input of days mapped to a list of times and returns a formatted string for the week.

## Requirements
- JDK 8 or higher (target is Java 8, but developed with open-JDK 11)
- Use gradle wrapper to avoid having to install Gradle, if using own install this has been tested with Gradle 5.2.1 

## Run

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

### Local API testing
The only API is mapped to `/parseHours` this takes a json map of String to a list of times, for example
```json
{
  "monday": [],
  "tuesday": [
    {
      "type": "open",
      "value": 36000
    },
    {
      "type": "close",
      "value": 64800
    }
  ],
  "wednesday": [],
  "thursday": [
    {
      "type": "open",
      "value": 36000
    },
    {
      "type": "close",
      "value": 64800
    }
  ],
  "friday": [
    {
      "type": "open",
      "value": 36000
    }
  ],
  "saturday": [
    {
      "type": "close",
      "value": 3600
    },
    {
      "type": "open",
      "value": 36000
    }
  ],
  "sunday": [
    {
      "type": "close",
      "value": 3600
    },
    {
      "type": "open",
      "value": 43200
    },
    {
      "type": "close",
      "value": 75600
    }
  ]
}
```

and it returns a formatted, human readable string
```text
Monday: Closed
Tuesday: 10 AM - 6 PM
Wednesday: Closed
Thursday: 10 AM - 6 PM
Friday: 10 AM - 1 AM
Saturday: 10 AM - 1 AM
Sunday: 12 PM - 9 PM
```

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
- Only partial application of Railway orientated programming style (Monads for errors), only few validation steps
- Some warnings in build time about mismatching kotlin versions in libs used, should be taken care of
- Currently if week starts with a "close" event output will fail with input validation error
- Cannot handle two consecutive 24 hour opening times (for example open monday, close wednesday will show tuesday as closed)
