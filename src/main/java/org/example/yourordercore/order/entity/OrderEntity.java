package org.example.yourordercore.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.yourordercore.order.status.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order_entity")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal amount; // сумма заказа
    @CreatedDate
    private LocalDateTime createdAt;
}
