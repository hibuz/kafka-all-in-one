# kafka-all-in-one

* https://github.com/confluentinc/cp-all-in-one
* https://github.com/provectus/kafka-ui


### command
```bash
# start containers
docker compose up

# Confluent(Kafka):                          8.0.1(4.0.x, 2025.10.01), 7.9.3(3.9.x), 7.8.2(3.8.x)
# Debezium(Kafka): 3.3.0(4.1.x, 2025.09.02), 3.2.3(4.0.x),             3.1.3(3.9.x)

# https://repo1.maven.org/maven2/io/debezium
wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/3.3.0.Final/debezium-connector-mysql-3.3.0.Final-plugin.tar.gz
wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/3.3.0.Final/debezium-connector-postgres-3.3.0.Final-plugin.tar.gz

# https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc
wget https://hub-downloads.confluent.io/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.8.4/confluentinc-kafka-connect-jdbc-10.8.4.zip

tar zxf debezium-connector-mysql* -C ./build
tar zxf debezium-connector-postgres* -C ./build

unzip confluentinc-kafka-connect-jdbc* -d ./build

# Jdbc(Source/Sink)Connector does not have mysql driver
cp build/debezium-connector-mysql/mysql-connector-j-*.jar build/confluentinc-kafka-connect-jdbc-10.8.4/lib/

# Restart connect to apply additionally installed connector plugins after server startup
docker restart connect


### Installed default connectors
```bash

docker exec -it -u root connect bash

# curl
microdnf install -y dnf

dnf install -y net-tools procps-ng jq

curl connect:8083/connector-plugins | jq .
[
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
