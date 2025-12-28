package com.hibuz.kafka.streams.examples.price;

import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;

public class PriceMonitorApplication {

    @Bean
    public KStream<String, GenericRecord> kStream(StreamsBuilder builder) {
        // Schema Registry 설정
        Map<String, String> serdeConfig = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
        // KafkaAvroSerde might not be present in all versions; build Serde from serializer/deserializer instead
        var kafkaAvroSerializer = new io.confluent.kafka.serializers.KafkaAvroSerializer();
        kafkaAvroSerializer.configure((Map) serdeConfig, false);
        var kafkaAvroDeserializer = new io.confluent.kafka.serializers.KafkaAvroDeserializer();
        kafkaAvroDeserializer.configure((Map) serdeConfig, false);
        Serde<Object> avroSerde = Serdes.serdeFrom(kafkaAvroSerializer, kafkaAvroDeserializer);

        // 상태 저장소 등록
        builder.addStateStore(Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("prices-store"),
                Serdes.String(),
                Serdes.String()
        ));

        KStream<String, GenericRecord> input = builder.stream("dbserver1.mysqldb.products",
                Consumed.with(Serdes.String(), (Serde) avroSerde));

        // 변동 탐지 Processor 적용 (processor 내부에서 alerts 토픽으로 직접 전송)
        input.process(() -> new PriceChangeProcessor(2.0), "prices-store"); // threshold 2%

        return input;
    }
}
