# kafka-all-in-one

* https://github.com/confluentinc/cp-all-in-one
* https://github.com/provectus/kafka-ui


### command
```bash
docker exec -it -u root control-center bash

microdnf install dnf

dnf install net-tools

dnf install procps-ng

```

#### Installed default connectors
```
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