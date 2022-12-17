## 주문 시스템 [![Build with Gradle](https://github.com/MJbae/order-system/actions/workflows/ci-script.yml/badge.svg)](https://github.com/MJbae/order-system/actions/workflows/ci-script.yml)
> CLI 기반 주문 시스템

### 실행 방법
프로젝트의 루트에서 이하의 명령 실행
```shell
docker build -t order-system:latest .
```
```shell
docker run -it -p 50152:50152 order-system:latest
```

### TDD 기반 구현 절차
1. 전체적인 로직을 직관적으로 파악하기 위해 [Flow Chart](https://github.com/MJbae/order-system/wiki/Flow-Cart) 작성
2. 가장 간단한 방식(Happy Path)의 주문 기능 구현
3. [Flow Chart](https://github.com/MJbae/order-system/wiki/Flow-Cart)에 작성한 주요 분기점에 대해 실패하는 단위 테스트 작성
4. 실패하는 테스트를 성공시키기 위해 코드 수정
5. 3번과 4번 과정을 충분히 반복한 후, 테스트 기반으로 전체적인 코드 리팩토링

### 프로젝트 구조
본 프로젝트는 이하의 5개 패키지로 구성
* cli: shell 환경의 입출력 처리
* application: 비지니스 로직 처리
* domain: 엔티티 정의
* exception: 커스텀 예외 정의
* infra: 영속적 데이터 처리

### 사용 기술
* Application Framework: Spring Shell
* RDBMS & ORM: H2, Spring Data JPA
* Test Framework: Kotest, JUnit

### 참고 자료
* Test 
  * Framework: [Kotest](https://kotest.io/)
  * Mocking: [MockK](https://mockk.io/)
  * BDD Style Test: [우형블로그](https://techblog.woowahan.com/5825/)
* Spring Shell: [Baeldung](https://www.baeldung.com/spring-shell-cli)
