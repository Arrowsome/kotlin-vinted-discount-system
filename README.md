### TDD Discount System in Kotlin ###

This project has been build following the Vinted instructions [here](https://gist.github.com/vintedEngineering/7a24d2bb2ef4189447c6b938604ab030)

The project uses Kotlin to bring a simple discount system with oop + functional programming paradigm into practice. I
believe the design could be flawed and improved upon significantly.

````shell
# For running project on the root directory execute:

$ ./gradlew build
$ ./gradlew run
````

#### Design Patterns

1. **CoR**: the discount system uses chain of responsibility to route shipment and apply discount on each entry.
2. **Service Locator**: is used as means of providing dependencies for the whole application.

#### Tests

There are a bunch of tests available that mostly have been written in test driven style

1. **Unit Tests**: for DAO and Rule classes
2. **Approval Testing**: for Processor class  

````shell
# For running tests on the root directory execute:

$ ./gradlew test
````

#### Dependencies

As requested no additional dependencies have been added beside Kotlin standard library unless it's
needed for testing.

1. **Mockk**: mocking objects while unit testing a class
2. **JUnit5**: Unit testing
3. **approvaltests**: approval testing
4. **test-logger**: to pretty print test outputs run in cli mode.

