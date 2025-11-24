package com.hibuz.kafka.streams.examples;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoKafkaListener {

	@KafkaListener(topics = "messages", groupId = "app1")
	public void listenWithApp1(String message) {
		log.info("Received message at app1: {}", message);
	}

	@KafkaListener(topics = "messages", groupId = "app2")
	public void listenWithApp2(String message) {
		log.info("Received message at app2: {}", message);
	}
}