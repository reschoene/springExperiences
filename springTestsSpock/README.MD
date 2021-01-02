# Demo project for testing a REST API using Spock and Spring Frameworks 
In this project, I explored the use of Spock Framework for conducting 
unit tests on my REST API developed using Spring Boot.

Bellow are the steps for compiling the project, running the tests 
and seeing statistics on SonarQube:

### 1) start SonarQube at localhost:9000
Open terminal in the project folder and type:

`docker-compose up`

Wait until "SonarQube is up" appears on the terminal.

### 2) download necessary packages, compile and run all tests (unit and integration tests)
Open another terminal window and in the project folder type: 

`mvn clean install`

### 3) upload tests results to SonarQube:
When project was successful built by the previous step, type:

`mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=admin`

### 4) Access sonar quality gate to see the tests results and coverage:
Open a browser and type `http://localhost:9000`. SonarQube will ask for your credentials.
The default username is `admin` and its password is `admin`. These are the credentials used as 
parameters on the previous step command. So, if you change your credentials and need to run step 3 again, change its parameters accordingly. 

## Additional Possibilities
* View generated spock test report
Go to folder `./build/spock-reports` and open the file `index.html` to view the Spock test report. 

* To run all unit tests:
`mvn test`

* To run integration and unit tests:
`mvn integration-test`

* For stopping SonarQube local instance
At the terminal window it was started, press `CTRL + c` for stopping it
and then type `docker-compose down`

