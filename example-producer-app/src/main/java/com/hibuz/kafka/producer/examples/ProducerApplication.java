package com.hibuz.kafka.producer.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class ProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }

  @KafkaListener(id = "payment-debug-consumer", topics = "${spring.kafka.topic.avro}")
  public void listen(Payment payment) {
      System.out.println(payment);
  }
}
