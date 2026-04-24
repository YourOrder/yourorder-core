package org.example.yourordercore.order.repository;

import org.example.yourordercore.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends
        JpaRepository<OrderEntity, UUID>,
        JpaSpecificationExecutor<OrderEntity> {
}
