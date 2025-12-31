package com.hibuz.kafka.producer.examples.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hibuz.kafka.core.examples.Payment;

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
}
