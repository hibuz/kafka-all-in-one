package com.hibuz.kafka.producer.examples;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }

  @KafkaListener(id = "payment-debug-consumer", topics = "${spring.kafka.topic.avro}")
  public void listen(ConsumerRecord<String, Payment> record) {
      Payment payment = record.value();
      log.info("Received message key-{}: id-{}, amount-{}", record.key(), payment.getId(), payment.getAmount());
  }
}
