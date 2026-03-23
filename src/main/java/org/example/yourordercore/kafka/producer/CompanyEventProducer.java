package org.example.yourordercore.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.yourordercore.kafka.event.CompanyCreatedEvent;
import org.example.yourordercore.kafka.event.CompanyUpdatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCompanyCreated(UUID companyId, UUID ownerId) {

        CompanyCreatedEvent event =
                new CompanyCreatedEvent(companyId, ownerId);

        kafkaTemplate.send("company-created", event);
    }

    public void sendCompanyUpdated(UUID companyId, UUID ownerId) {

        CompanyUpdatedEvent event =
                new CompanyUpdatedEvent(companyId, ownerId);

        kafkaTemplate.send("company-updated", event);
    }
}