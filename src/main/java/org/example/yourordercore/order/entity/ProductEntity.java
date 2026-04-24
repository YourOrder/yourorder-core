package org.example.yourordercore.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    @Positive
    private BigDecimal price;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id",  nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    @Builder.Default
    private Integer reservedQuantity = 0;

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public void reserve(int amount) {
        validatePositiveAmount(amount);

        if (getAvailableQuantity() < amount) {
            throw new IllegalStateException("Not enough stock");
        }

        reservedQuantity += amount;
    }

    public void releaseReservation(int amount) {
        validatePositiveAmount(amount);

        if (reservedQuantity < amount) {
            throw new IllegalStateException("Cannot release more than reserved");
        }

        reservedQuantity -= amount;
    }

    public void confirmReservation(int amount) {
        validatePositiveAmount(amount);

        if (reservedQuantity < amount) {
            throw new IllegalStateException("Cannot confirm more than reserved");
        }

        reservedQuantity -= amount;
        quantity -= amount;
    }

    private void validatePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
