package com.hibuz.kafka.streams.examples.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.hibuz.kafka.core.examples.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Receiver {

  @KafkaListener(topics = "${spring.kafka.topic.avro}")
  public void listen(Payment value) {
      log.info("Received message: id={}, amount={}", value.getId(), value.getAmount());
  }
}
