package org.example.yourordercore.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.yourordercore.kafka.event.CompanyCreatedEvent;
import org.example.yourordercore.kafka.event.CompanyUpdatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCompanyCreated(UUID companyId, UUID ownerId) {

        CompanyCreatedEvent event =
                new CompanyCreatedEvent(companyId, ownerId);

        log.info("🚀 Sending CompanyCreatedEvent: {}", event);

        kafkaTemplate.send("company-created", event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Failed to send CompanyCreatedEvent", ex);
                    } else {
                        log.info("✅ Event sent to topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void sendCompanyUpdated(UUID companyId, UUID ownerId) {

        CompanyUpdatedEvent event =
                new CompanyUpdatedEvent(companyId, ownerId);

        kafkaTemplate.send("company-updated", event);
    }
}