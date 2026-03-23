package org.example.yourordercore.kafka.event;

import java.util.UUID;

public record CompanyUpdatedEvent(
        UUID companyId,
        UUID ownerId
) {}
