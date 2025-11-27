package com.hibuz.kafka.streams.examples;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoKafkaListener {

	@Value("${spring.application.name}")
	private String appName;

	@KafkaListener(topics = "test-topic")
	public void listenWithApp(String message, ConsumerRecordMetadata metadata) {
		log.info("Received message at {}, p-{}, offset-{}: {}", appName, metadata.partition(), metadata.offset(), message);
	}
}