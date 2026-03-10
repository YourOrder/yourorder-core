package org.example.yourordercore.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private UUID productId;
    @Positive
    private Integer quantity;
}
