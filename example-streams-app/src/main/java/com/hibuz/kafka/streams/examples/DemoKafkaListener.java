package com.hibuz.kafka.streams.examples;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoKafkaListener {

	@Value("${spring.kafka.consumer.client-id}")
	private String clientId;

	@KafkaListener(id = "test-group", topics = "test-topic",
				   properties = {"value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"})
	public void listenWithApp(String message, ConsumerRecordMetadata metadata) {
		log.info("Received message at {}, p={}, offset={}: message={}", clientId, metadata.partition(), metadata.offset(), message);
	}
}