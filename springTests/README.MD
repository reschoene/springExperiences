#Demo project for testing a REST API in an Spring Project
This project uses JUnit5 (https://junit.org/junit5/) as a testing framework, Mockito as a mock framework (https://site.mockito.org/) and Spring as a extension to the JUnit5 (SpringExtension).

The integration tests was developed using SpringBootTest, which starts a complete spring context, allowing each test to be done over the entire solution.

The unit tests was conduct over the individual parts and its dependencies was mocked using mockito.

### - To run all unit tests:
`mvn test -Punit-tests`

### - To run all integration tests:
`mvn test -Pintegration-tests`

### - To run all tests:
`mvn test -Pall-tests`

