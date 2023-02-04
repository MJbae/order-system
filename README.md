## CLI based Order System [![Build with Gradle](https://github.com/MJbae/order-system/actions/workflows/ci-script.yml/badge.svg)](https://github.com/MJbae/order-system/actions/workflows/ci-script.yml)

### How to run
Run the following commands from the root of the project
```shell
docker build -t order-system:latest .
```
```shell
docker run -it -p 50152:50152 order-system:latest
```

### Implementation Procedure based on TDD
1. Write a [Flow Chart](https://github.com/MJbae/order-system/wiki/Flow-Cart) to grasp the overall logic intuitively.
2. Implement the ordering function in the simplest way
3. Write a test that fails for the main branch points written in the [Flow Chart](https://github.com/MJbae/order-system/wiki/Flow-Cart)
4. Modify the code to make the failing test pass.
5. Repeat steps 3 and 4 enough times, then refactor the entire code based on the test.

### Project Structure
This project is composed of the following 5 packages
* cli: defines objects responsible for input and output in the command environment.
* application: defines the application service.
* domain: defines domain objects.
* exception: defines custom exceptions.
* infra: defines objects responsible for persistent data.

### Key Technologies
* Application Framework: Spring Shell
* RDBMS & ORM: H2, Spring Data JPA
* Test Framework: Kotest

### References
* Application Framework: Spring Shell, [Baeldung Blog](https://www.baeldung.com/spring-shell-cli)
* Test Framework: Kotest, [Official Document](https://kotest.io/)
* Test Style: BDD Test Style, [BaeMain Blog](https://techblog.woowahan.com/5825/)
* Branch Coverage Plugin: JaCoCo, [Official Document](https://docs.gradle.org/7.4.2/userguide/jacoco_plugin.html), [BaeMain Blog](https://techblog.woowahan.com/2661/)
* Mocking Tool: MockK, [Official Document](https://mockk.io/)
