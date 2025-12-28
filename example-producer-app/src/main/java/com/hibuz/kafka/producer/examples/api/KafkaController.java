package com.hibuz.kafka.producer.examples.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hibuz.kafka.producer.examples.Payment;
import com.hibuz.kafka.producer.examples.sender.Sender;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final Sender sender;

    @PostMapping("payment")
    public void sendPayment(String amount) {
        Payment payment = Payment.newBuilder()
                .setId("demo-web-0")
                .setAmount(Double.valueOf(amount))
                .build();

        sender.send("web-key1", payment);
    }
}
