# example-streams-app

한국어 (KR) / English (EN) 상세 설명

## 개요 / Overview
- **KR**: 이 예제 애플리케이션은 로컬 Kafka 브로커에 연결되어 `messages` 토픽을 리스닝하는 Spring Boot 애플리케이션입니다. `DemoKafkaListener` 클래스는 동일 토픽에 대해 서로 다른 consumer group(`app1`, `app2`)을 사용하여 각각 메시지를 수신하고 로그로 출력합니다.
- **EN**: This example application is a Spring Boot app that connects to a local Kafka broker and listens to the `messages` topic. The `DemoKafkaListener` class uses two different consumer groups (`app1` and `app2`) so both listeners receive copies of messages and log them.

## 요구 사항 / Requirements
- **Java**: Java 21 (build toolchain in `build.gradle` targets Java 21)
- **Gradle Wrapper**: Use the included `./gradlew` wrapper
- **Docker & docker compose**: For local Kafka and related services
- **Ports used (host -> container)**: `9092` (Kafka), `8081` (Schema Registry), `8083` (Kafka Connect), `8088` (ksqlDB), `8989` (Kafka UI), `3306` (MySQL), `5432` (Postgres)

## 빠른 시작 / Quick Start

1. 로컬 Kafka 스택을 띄웁니다 (레포지토리 루트에 있는 `docker-compose.yml` 또는 전체 구성 `docker-compose-full.yml` 사용 권장).

```bash
# 루트에서
docker compose up -d
# 또는 전체 구성:
docker compose -f docker-compose-full.yml up -d
```

2. 애플리케이션 실행 (Gradle bootRun):

```bash
./gradlew :example-streams-app:bootRun
```

3. 애플리케이션이 브로커(`localhost:9092`)에 연결되면 `DemoKafkaListener`가 `messages` 토픽을 리스닝합니다. 토픽이 없다면 compose 초기화 스크립트가 생성하지만, 수동으로 생성하려면 아래를 참고하세요.

## 빌드 및 실행(대안) / Build & Run (alternate)

- 빌드 후 JAR 실행:

```bash
./gradlew :example-streams-app:clean :example-streams-app:build
java -jar example-streams-app/build/libs/*example-streams-app*.jar
```

- 컨테이너 이미지 생성 (Paketo buildpacks 사용 설정이 `build.gradle`에 있음):

```bash
./gradlew :example-streams-app:bootBuildImage
```

## 테스트: `messages` 토픽으로 메시지 보내기 / Test: send messages to `messages` topic

1. Docker 내부에서 Kafka 콘솔 프로듀서를 사용 (가장 쉬운 방법):

```bash
# Kafka 컨테이너 이름은 docker-compose 설정에 따라 다릅니다. 예: kafka
docker compose exec broker kafka-console-producer --bootstrap-server localhost:9092 --topic messages
> Hello from CLI
```

2. 로컬 터미널에서 `kcat` 혹은 다른 Kafka 클라이언트를 사용해도 됩니다.

3. 예상 로그 출력 (애플리케이션 로그):

```
INFO  - Received message at app1: Hello from CLI
INFO  - Received message at app2: Hello from CLI
```

두 리스너는 같은 토픽을 서로 다른 consumer group으로 구독하고 있으므로 각 리스너가 메시지를 독립적으로 수신합니다.

## 데이터베이스 및 초기화 스크립트 / DB init
- 초기 데이터베이스 SQL 파일은 `data/`에 있습니다 (`mysqldb-init.sql`, `pgdb-init.sql`). Compose 설정은 이 파일들을 사용해 컨테이너 시작 시 초기화합니다.

## 문제 해결 / Troubleshooting
- 브로커에 연결할 수 없음: `localhost:9092`가 열려 있는지, `docker compose ps`로 컨테이너가 기동 중인지 확인하세요.
- 토픽 미생성: `docker compose exec broker kafka-topics --bootstrap-server localhost:9092 --list`로 확인, 필요 시 생성:

```bash
docker compose exec broker kafka-topics --bootstrap-server localhost:9092 --create --topic messages --partitions 1 --replication-factor 1
```

- Java 버전 오류: 로컬 JDK가 Java 21인지 확인하세요. Gradle wrapper가 로컬 JDK를 사용하도록 설정되어 있습니다.

## 파일 및 경로 요약 / Files & Useful Paths
- 애플리케이션 소스: `example-streams-app/src/main/java/` (`DemoKafkaListener`는 `com.hibuz.kafka.streams.examples` 패키지)
- 빌드 스크립트: `example-streams-app/build.gradle`
- 커넥터 설정: `connectors/` 디렉터리
- DB 초기화: `data/` 디렉터리
- 전체 도커 구성: `docker-compose.yml`, `docker-compose-full.yml`

## 추가 참고 / Notes
- `DemoKafkaListener`는 다음과 같은 메서드를 제공합니다 (간단 요약):
  - `listenWithApp1(String message)` — `@KafkaListener(topics = "messages", groupId = "app1")`
  - `listenWithApp2(String message)` — `@KafkaListener(topics = "messages", groupId = "app2")`

---

If you want, I can also add a short quick-start script or a sample `docker compose` command snippet that pre-creates topics and connectors automatically. Let me know and I'll append it.
