package com.hibuz.kafka.streams.examples.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.requests.CreateTopicsRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableKafka
public class KafkaConfig {
public static final String TOPIC_NAME = "test-topic";

	@Bean
	public NewTopic testTopic() {
		return new NewTopic(TOPIC_NAME, 2, CreateTopicsRequest.NO_REPLICATION_FACTOR);
	}
}
