# example-producer-app

한국어 (KR) / English (EN) 상세 설명

## 개요 / Overview
- **KR**: 이 예제 애플리케이션은 로컬 Kafka 브로커에 연결되어 `payment-demo` 토픽으로 메시지를 전송하는 Spring Boot 애플리케이션입니다. `Receiver` 클래스는 디버그를 위한 컨슈머로, 동일 토픽에서 메시지를 수신하고 로그로 출력합니다.
- **EN**: This example application is a Spring Boot app that connects to a local Kafka broker and sends messages to the `payment-demo` topic. The `Receiver` class acts as a debug consumer that listens to the same topic and logs received messages.

## 요구 사항 / Requirements
- **Java**: Java 21 (build toolchain in `build.gradle` targets Java 21)
- **Gradle Wrapper**: Use the included `./gradlew` wrapper
- **Docker & docker compose**: For local Kafka and related services
- **Ports used (host -> container)**: `8080` (Spring Boot), `9092` (Kafka), `8081` (Schema Registry)

## 빠른 시작 / Quick Start

1. 애플리케이션 실행 (Gradle bootRun):

```bash
./gradlew :example-producer-app:bootRun
```

3. 애플리케이션이 브로커(`localhost:9092`)에 연결되면 `Receiver`가 `payment-demo` 토픽을 리스닝합니다.

## 빌드 및 실행(대안) / Build & Run (alternate)

- 빌드 후 JAR 실행:

```bash
./gradlew :example-producer-app:clean :example-producer-app:build
java -jar example-producer-app/build/libs/*example-producer-app*.jar
```

- 컨테이너 이미지 생성 (Paketo buildpacks 사용 설정이 `build.gradle`에 있음):

```bash
./gradlew :example-producer-app:bootBuildImage
```

## 테스트: `payment-demo` 토픽으로 메시지 보내기 / Test: send messages to `payment-demo` topic

1. cURL을 사용해 HTTP 엔드포인트로 메시지 전송:

```bash
curl -X POST 127.0.0.1:8080/kafka/payment?amount=1
```

2. 예상 로그 출력 (애플리케이션 로그):
```
INFO : Received message: id-init0, amount-10.0
INFO : Received message: id-demo-web-0, amount-1.0
```