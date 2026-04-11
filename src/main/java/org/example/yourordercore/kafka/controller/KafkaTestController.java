package org.example.yourordercore.kafka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.yourordercore.kafka.producer.CompanyEventProducer;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Profile({"dev", "docker"})
@Tag(name = "Kafka Test", description = "Endpoints for testing Kafka integration. Available only in dev and docker profiles.")
public class KafkaTestController {

    private final CompanyEventProducer producer;

    @Operation(
            summary = "Test Kafka event",
            description = "Sends a test Kafka event with random UUIDs. Available only in dev and docker profiles."
    )
    @GetMapping("/test-kafka")
    public String testKafka() {
        producer.sendCompanyCreated(
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        return "Kafka event sent";
    }
}