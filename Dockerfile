FROM openjdk:11-jdk-slim AS builder

ENV PROJECT_NAME order-system
ENV HOME /usr/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:11
COPY --from=builder /build/libs/*.jar ${PROJECT_NAME}.jar

EXPOSE 50152
ENTRYPOINT java -jar ${PROJECT_NAME}.jar
