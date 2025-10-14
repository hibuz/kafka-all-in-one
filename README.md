# kafka-all-in-one
A simple Apache Kafka environment with Kafka Connect, Schema Registry, ksqlDB, Kafka UI, MySQL, and PostgreSQL using Docker Compose.

### Installation
```bash
# Confluent(Kafka):                          8.0.1(4.0.x, 2025.10.01), 7.9.3(3.9.x), 7.8.2(3.8.x)
# Debezium(Kafka): 3.3.0(4.1.x, 2025.09.02), 3.2.3(4.0.x),             3.1.3(3.9.x)

# start containers (Add the -f docker-compose-full.yml option to create a sample connector automatically)
docker compose up
```

### Check available connectors
```bash

docker exec -it -u root connect bash

# curl
microdnf install -y dnf

dnf install -y net-tools procps-ng jq

curl connect:8083/connector-plugins | jq .
[
  {
    "class": "com.hibuz.kafka.connect.examples.RandomSourceConnector",
    "type": "source",
    "version": "1.0.0"
  },
  {
    "class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "type": "sink",
    "version": "10.8.4"
  },
  {
    "class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "type": "source",
    "version": "10.8.4"
  },
  {
    "class": "io.confluent.kafka.connect.datagen.DatagenConnector",
    "type": "source",
    "version": "null"
  },
  {
    "class": "io.debezium.connector.mysql.MySqlConnector",
    "type": "source",
    "version": "3.3.0.Final"
  },
  {
    "class": "io.debezium.connector.postgresql.PostgresConnector",
    "type": "source",
    "version": "3.3.0.Final"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorCheckpointConnector",
    "type": "source",
    "version": "8.0.0-ccs"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorHeartbeatConnector",
    "type": "source",
    "version": "8.0.0-ccs"
  },
  {
    "class": "org.apache.kafka.connect.mirror.MirrorSourceConnector",
    "type": "source",
    "version": "8.0.0-ccs"
  }
]
```

## Visit 
- Kafka UI: http://localhost:8989
![KafkaUI-RandomSourceConnector](.assets/kafka-ui.png)
- Control Center UI: http://localhost:9021 (full version only)
![ControlCenter-RandomSourceConnector](.assets/cc.png)

### Stops containers and removes containers, networks, and volumes created by `compose up`.
```bash
docker compose down -v
[+] Running 11/11
 ✔ Container kafka-all-in-one-kafka-init-topics-1  Removed
 ✔ Container postgres                              Removed
 ✔ Container mysql                                 Removed
 ✔ Container kafka-ui                              Removed
 ✔ Container ksqldb-server                         Removed
 ✔ Container connect                               Removed
 ✔ Container schema-registry                       Removed
 ✔ Container broker                                Removed
 ✔ Volume kafka-all-in-one_mysql-vol               Removed
 ✔ Volume kafka-all-in-one_pg-vol                  Removed
 ✔ Network kafka-all-in-one_default                Removed
```

## References
* https://github.com/confluentinc/cp-all-in-one
* https://github.com/provectus/kafka-ui
* https://github.com/enfuse/kafka-connect-demo