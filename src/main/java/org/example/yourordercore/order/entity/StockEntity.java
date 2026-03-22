package org.example.yourordercore.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "stock_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private ProductEntity product;

    @Column(nullable = false)
    @PositiveOrZero
    @Builder.Default
    private Integer quantity = 0;

    @Column(name = "reserved_quantity",nullable = false)
    @PositiveOrZero
    @Builder.Default
    private Integer reservedQuantity = 0;

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public boolean canReserve(int amount) {
        validatePositiveAmount(amount);
        return getAvailableQuantity() >= amount;
    }

    public void reserve(int amount) {
        if (!canReserve(amount)) {
            throw new IllegalStateException("Cannot reserve " + amount + " items. Available: " + getAvailableQuantity());
        }
        this.reservedQuantity += amount;
    }

    public void releaseReservation(int amount) {
        validatePositiveAmount(amount);
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Cannot release " + amount + " items. Reserved: " + reservedQuantity);
        }
        this.reservedQuantity -= amount;
    }

    public void confirmReservation(int amount) {
        validatePositiveAmount(amount);
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException(
                    "Cannot confirm " + amount + " items. Reserved: " + reservedQuantity
            );
        }
        this.reservedQuantity -= amount;
        this.quantity -= amount;
    }

    private void validatePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
