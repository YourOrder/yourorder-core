package org.example.yourordercore.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.example.yourordercore.kafka.producer.CompanyEventProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class KafkaTestController {

    private final CompanyEventProducer producer;

    @GetMapping("/test-kafka")
    public String testKafka() {
        producer.sendCompanyCreated(
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        return "Kafka event sent";
    }
}