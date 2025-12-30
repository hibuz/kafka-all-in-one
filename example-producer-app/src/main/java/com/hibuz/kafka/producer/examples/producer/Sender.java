package com.hibuz.kafka.producer.examples.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hibuz.kafka.producer.examples.Payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class Sender {

    @Value("${spring.kafka.topic.avro}")
    private String avroTopic;

    private final KafkaTemplate<String, Payment> template;

    public void send(String key, Payment value) {
        this.template.send(avroTopic, key, value);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generate() {
    Payment payment = Payment.newBuilder()
        .setId("init0")
        .setAmount(10.0)
        .build();

      send("init-key0", payment);
    }
}
