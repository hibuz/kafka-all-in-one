package com.hibuz.kafka.producer.examples.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hibuz.kafka.core.examples.Payment;
import com.hibuz.kafka.producer.examples.producer.Sender;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final Sender sender;

    @PostMapping("payment")

    public void sendPayment(@RequestParam(defaultValue = "0.0") String amount,
                            @RequestParam(defaultValue = "kr") String region,
                            @RequestParam(defaultValue = "krw") String currency) {
        double amt = Double.parseDouble(amount);
        Payment payment = Payment.newBuilder()
                .setId("id0")
                .setAmount(amt)
                .build();

        sender.send("key0", payment);
    }
}
