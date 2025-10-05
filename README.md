# kafka-all-in-one

* https://github.com/confluentinc/cp-all-in-one
* https://github.com/provectus/kafka-ui


### command
```bash
# start containers
docker compose up

# stop containers ()
docker compose down -v

[+] Running 3/3
 ✔ Container hbase         Removed
 ✔ Volume hbase_hbase-vol  Removed
 ✔ Network hbase_default   Removed

```


## Reference
### Visit 
- Kafka UI: http://localhost:8989

### Installed default connectors
```bash

docker exec -it -u root broker bash

# curl
microdnf install -y dnf

dnf install -y net-tools

dnf install -y procps-ng

curl connect:8083/connector-plugins

[
    {
        "class": "io.confluent.kafka.connect.datagen.DatagenConnector",
        "type": "source",
        "version": "null"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorCheckpointConnector",
        "type": "source",
        "version": "7.6.0-ce"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorHeartbeatConnector",
        "type": "source",
        "version": "7.6.0-ce"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorSourceConnector",
        "type": "source",
        "version": "7.6.0-ce"
    }
]
```