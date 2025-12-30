package com.hibuz.kafka.producer.examples.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hibuz.kafka.producer.examples.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Receiver {

  @KafkaListener(id = "payment-debug-consumer", topics = "${spring.kafka.topic.avro}")
  public void listen(Payment value) {
      log.info("Received message: id-{}, amount-{}", value.getId(), value.getAmount());
  }
}
