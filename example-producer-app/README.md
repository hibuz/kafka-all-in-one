# example-producer-app

한국어 (KR) / English (EN) 상세 설명

## 개요 / Overview
- **KR**: 이 예제 애플리케이션은 로컬 Kafka 브로커에 연결되어 `transactions` 토픽으로 메시지를 전송하는 Spring Boot 애플리케이션입니다. `Receiver` 클래스는 디버그를 위한 컨슈머로, 동일 토픽에서 메시지를 수신하고 로그로 출력합니다.
- **EN**: This example application is a Spring Boot app that connects to a local Kafka broker and sends messages to the `transactions` topic. The `Receiver` class acts as a debug consumer that listens to the same topic and logs received messages.

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

3. 애플리케이션이 브로커(`localhost:9092`)에 연결되면 `Receiver`가 `transactions` 토픽을 리스닝합니다.

## 빌드 및 실행(대안) / Build & Run (alternate)

- 빌드 후 JAR 실행:

```bash
./gradlew :example-producer-app:clean :example-producer-app:build
java -jar example-producer-app/build/libs/*example-producer-app*.jar --spring.profiles.active=prd (Production only)
```

- 컨테이너 이미지 생성 (Paketo buildpacks 사용 설정이 `build.gradle`에 있음):

```bash
./gradlew :example-producer-app:bootBuildImage

docker images
REPOSITORY                                           TAG       IMAGE ID       CREATED        SIZE
paketobuildpacks/ubuntu-noble-run-tiny               0.0.47    9573df49a1c9   4 weeks ago    38.9MB
paketobuildpacks/builder-noble-java-tiny             latest    7fc39dc25d0d   46 years ago   1.19GB
ghcr.io/hibuz/example-producer-app                   latest    e6274cf4de43   46 years ago   755MB

docker login ghcr.io -u Dhibuz --password <TOKEN>

docker push ghcr.io/hibuz/example-producer-app:latest
```

## 테스트: `transactions` 토픽으로 메시지 보내기 / Test: send messages to `transactions` topic

1. cURL을 사용해 HTTP 엔드포인트로 메시지 전송 테스트
```bash
curl -X POST localhost:8080/kafka/payment?amount=1

# Expected log output (application log):
INFO : Received message: id=id0, amount=1.0
```

2. 하위 호환성(Backword compatibility) 테스트
- Counsumer가 새로운 스키마로 이전 데이터(마지막으로 등록된 스키마를 사용하는 Producer)를 읽을 수 있는지 확인
- 소비자(Consumer) 중심의 업데이트: 필드 삭제, (기본값이 있는 필드를 추가) 허용

- schema 변경
```yaml
# amount -> 삭제
# region -> 추가 (기본값: "")
    {
      "name": "region",
      "default": "kr",
      "type": {
        "avro.java.string": "String",
        "type": "string"
      }
    }
```
- 테스트
```bash
# 이전 스키마를 사용하는 Producer로 메시지 전송
curl -X POST localhost:8080/kafka/payment?amount=1
# Expected log output (application log):
INFO : Received message: id=id0, region=kr

# Producer를 새로운 스키마로 업데이트 후 메시지 전송
curl -X POST localhost:8080/kafka/payment?region=us
# Expected log output (application log):
INFO : Received message: id=id0, region=us
```

3. 상위 호환성(Forward compatibility) 테스트
- Producer가 새로운 스키마로 이전 데이터를 소비하는(마지막으로 등록된 스키마를 사용) Consumer 중단없이 쓸 수 있는지 확인
- 생산자(Producer) 중심의 업데이트: 필드 추가, (기본값이 있는 필드를 삭제) 허용

- schema 변경
```yaml
# region -> 삭제 (기본값: "")
# currency -> 추가
    {
      "name": "currency",
      "type": {
        "avro.java.string": "String",
        "type": "string"
      }
    }
```
- 테스트
```bash
# 이전 스키마를 사용하는 Consumer로 메시지 전송
curl -X POST localhost:8080/kafka/payment?currency=krw
# 500 에러 발생 예상:
ERROR : {"timestamp":"2026-01-03T22:43:32.451Z","status":500,"error":"Internal Server Error","path":"/kafka/payment"}

# Control Center UI > Schema compatibility settings > "Forward" 로 설정 후 재시도 
# Expected log output (application log):
INFO : Received message: id=id0, region=kr

# Consumer를 새로운 스키마로 업데이트 후 메시지 전송
curl -X POST localhost:8080/kafka/payment?currency=krw 
# Expected log output (application log):
INFO : Received message: id=id0, currency=krw
```
## Visit 
- Kafka UI: http://localhost:8989
- Control Center UI: http://localhost:9021

### Stops containers and removes containers, networks, and volumes created by `compose up`.
```bash
docker compose down -v

## References
* https://github.com/confluentinc/cp-all-in-one