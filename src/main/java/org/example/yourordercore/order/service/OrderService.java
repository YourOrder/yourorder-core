package org.example.yourordercore.order.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.yourordercore.order.dto.OrderItemRequest;
import org.example.yourordercore.order.dto.OrderRequest;
import org.example.yourordercore.order.entity.OrderEntity;
import org.example.yourordercore.order.entity.OrderItemEntity;
import org.example.yourordercore.order.entity.ProductEntity;
import org.example.yourordercore.order.entity.StockEntity;
import org.example.yourordercore.order.repository.OrderRepository;
import org.example.yourordercore.order.repository.ProductRepository;
import org.example.yourordercore.order.repository.StockRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public OrderEntity createOrder(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Order request cannot be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        OrderEntity order = OrderEntity.builder()
                .userId(request.getUserId())
                .build();

        for (OrderItemRequest itemRequest : request.getItems()) {
            if (itemRequest == null) {
                throw new IllegalArgumentException("Order item request cannot be null");
            }
            if (itemRequest.getProductId() == null) {
                throw new IllegalArgumentException("Product id cannot be null");
            }
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found: " + itemRequest.getProductId()
                    ));

            StockEntity stock = stockRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Stock not found for product: " + product.getId()
                    ));

            stock.reserve(itemRequest.getQuantity());

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            order.addItem(orderItem);
        }
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderEntity getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public OrderEntity confirmOrder(UUID orderId) {
        OrderEntity order = getOrderById(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    public OrderEntity cancelOrder(UUID orderId) {
        OrderEntity order = getOrderById(orderId);

        order.getItems().forEach(item -> {
            StockEntity stock = stockRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Stock not found for product: " + item.getProduct().getId()
                    ));
            stock.releaseReservation(item.getQuantity());
        });

        order.cancel();
        return orderRepository.save(order);
    }

    public OrderEntity markOrderAsPaid(UUID orderId) {
        OrderEntity order = getOrderById(orderId);

        order.getItems().forEach(item -> {
            StockEntity stock = stockRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Stock not found for product: " + item.getProduct().getId()
                    ));
            stock.confirmReservation(item.getQuantity());
        });

        order.markAsPaid();
        return orderRepository.save(order);
    }
}
