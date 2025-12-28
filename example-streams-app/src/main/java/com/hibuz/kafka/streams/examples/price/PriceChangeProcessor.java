package com.hibuz.kafka.streams.examples.price;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PriceChangeProcessor implements Processor<String, GenericRecord, String, String> {
    private ProcessorContext<String, String> context;
    private KeyValueStore<String, String> store;
    private final double thresholdPercent;
    private final ObjectMapper mapper = new ObjectMapper();
    private Producer<String, String> producer;
    private final String alertsTopic = "product-price-alerts";

    public PriceChangeProcessor(double thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    @Override
    public void init(ProcessorContext<String, String> context) {
        this.context = context;
        this.store = (KeyValueStore<String, String>) context.getStateStore("prices-store");

        Map<String, Object> appConfigs = context.appConfigs();
        Object bs = appConfigs.get("bootstrap.servers");
        String bootstrapServers = bs != null ? bs.toString() : "broker:29092";

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }

    @Override
    public void process(Record<String, GenericRecord> record) {
        if (record == null) return;

        GenericRecord value = record.value();
        String key = record.key();
        Object idObj = value.get("id");
        String id = idObj != null ? idObj.toString() : key;
        Object priceObj = value.get("price");

        BigDecimal newPrice = parsePrice(priceObj, 3); // scale = 3 (schema에 맞게)
        if (newPrice == null) return;

        String prevStr = store.get(id);
        BigDecimal prevPrice = prevStr != null ? new BigDecimal(prevStr) : null;

        store.put(id, newPrice.toPlainString());

        if (prevPrice == null) {
            // initial insert: no alert
            return;
        }

        if (prevPrice.compareTo(BigDecimal.ZERO) == 0) return;

        BigDecimal changePercent = newPrice.subtract(prevPrice)
                .divide(prevPrice, 6, BigDecimal.ROUND_HALF_UP)
                .abs()
                .multiply(BigDecimal.valueOf(100));

        if (changePercent.doubleValue() >= thresholdPercent) {
            Map<String,Object> alert = new HashMap<>();
            alert.put("id", id);
            alert.put("name", value.get("name") != null ? value.get("name").toString() : null);
            alert.put("oldPrice", prevPrice);
            alert.put("newPrice", newPrice);
            alert.put("changePercent", changePercent);
            try {
                String payload = mapper.writeValueAsString(alert);
                log.info("{} alert -> {}", id, payload);
                producer.send(new ProducerRecord<>(alertsTopic, id, payload));
            } catch (JsonProcessingException e) {
                log.error("JSON processing error", e);
            }
        }
    }

    @Override
    public void close() {
        if (producer != null) producer.close();
    }

    private static BigDecimal parsePrice(Object priceObj, int scale) {
        if (priceObj == null) return null;
        if (priceObj instanceof BigDecimal) {
            return ((BigDecimal) priceObj).setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        if (priceObj instanceof ByteBuffer) {
            ByteBuffer bb = (ByteBuffer) priceObj;
            byte[] bytes = new byte[bb.remaining()];
            bb.get(bytes);
            return new BigDecimal(new java.math.BigInteger(bytes), scale);
        }
        if (priceObj instanceof byte[]) {
            return new BigDecimal(new java.math.BigInteger((byte[]) priceObj), scale);
        }
        // fallback
        try {
            return new BigDecimal(priceObj.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }
}