package org.example.yourordercore.kafka.event;

import java.util.UUID;

public record CompanyCreatedEvent(
        UUID companyID,
        UUID owner
) {}
