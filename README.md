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
* cli: command 환경의 입출력 담당 객체 정의
* application: 어플리케이션 서비스 정의
* domain: 도메인 객체 정의
* exception: 커스텀 예외 정의
* infra: 영속적 데이터 담당 객체 정의

### 사용 기술
* Application Framework: Spring Shell
* RDBMS & ORM: H2, Spring Data JPA
* Test Framework: Kotest

### 참고 자료
* Application Framework: Spring Shell, [Baeldung 블로그 참고](https://www.baeldung.com/spring-shell-cli)
* Test Framework: Kotest, [공식문서 참고](https://kotest.io/)
* Test Style: BDD Test Style, [우형블로그 참고](https://techblog.woowahan.com/5825/)
* Branch Coverage Plugin: JaCoCo, [Gradle 문서 설정 참고](https://docs.gradle.org/7.4.2/userguide/jacoco_plugin.html), [우형블로그 사용법 참고](https://techblog.woowahan.com/2661/)
* Mocking Tool: MockK, [공식문서 참고](https://mockk.io/)
