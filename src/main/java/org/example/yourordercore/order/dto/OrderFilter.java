package org.example.yourordercore.order.dto;

import lombok.Data;
import org.example.yourordercore.order.status.OrderStatus;

import java.util.UUID;

@Data
public class OrderFilter {
    private UUID userId;
    private OrderStatus status;
}
