package com.hibuz.kafka.connect.examples.config;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class RandomConnectorConfig extends AbstractConfig {

    public static final String CONNECTOR_VERSION = "1.0.0";

    public static final Schema VALUE_SCHEMA = SchemaBuilder.struct().name("random_value")
            .version(1)
            .field("value", Schema.INT32_SCHEMA)
            .build();

    public static final String TOPIC_CONFIG = "topic";

    public static final String SLEEP_CONFIG = "sleep.seconds";

    public RandomConnectorConfig(Map<?, ?> originals) {
        super(config(), originals);
    }

    public static ConfigDef config() {
        return new ConfigDef()
            .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Topic name")
            .define(SLEEP_CONFIG, ConfigDef.Type.INT, 1, ConfigDef.Importance.LOW, "Sleep seconds");
    }
}
