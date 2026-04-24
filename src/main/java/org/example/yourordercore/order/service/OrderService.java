package org.example.yourordercore.order.service;

import lombok.RequiredArgsConstructor;
import org.example.yourordercore.exception.BadRequestException;
import org.example.yourordercore.exception.ConflictException;
import org.example.yourordercore.exception.NotFoundException;
import org.example.yourordercore.order.dto.OrderFilter;
import org.example.yourordercore.order.dto.OrderItemRequest;
import org.example.yourordercore.order.dto.OrderRequest;
import org.example.yourordercore.order.entity.OrderEntity;
import org.example.yourordercore.order.entity.OrderItemEntity;
import org.example.yourordercore.order.entity.ProductEntity;
import org.example.yourordercore.order.repository.OrderRepository;
import org.example.yourordercore.order.repository.ProductRepository;
import org.example.yourordercore.order.specification.OrderSpecification;
import org.example.yourordercore.order.status.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private static final int MAX_PAGE_SIZE = 50;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderEntity createOrder(OrderRequest request) {
        OrderEntity order = OrderEntity.builder()
                .userId(request.getUserId())
                .build();

        for (OrderItemRequest itemRequest : request.getItems()) {
            processOrderItem(order, itemRequest);
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderEntity getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new NotFoundException("Order not found")
                );
    }

    public OrderEntity confirmOrder(UUID orderId) {
        OrderEntity order = getOrderById(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    public OrderEntity cancelOrder(UUID orderId) {
        OrderEntity order = getOrderById(orderId);

        order.cancel();

        order.getItems().forEach(item ->
                item.getProduct().releaseReservation(item.getQuantity())
        );

        return orderRepository.save(order);
    }

    public OrderEntity pay(UUID orderId) {
        OrderEntity order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.PAID) {
            return order;
        }

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new ConflictException("Only confirmed orders can be paid");
        }

        if (order.getItems().isEmpty()) {
            throw new NotFoundException("Order has no items");
        }

        order.getItems().forEach(item ->
                item.getProduct().confirmReservation(item.getQuantity())
        );

        order.markAsPaid();

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderEntity> getOrders(OrderFilter filter, Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size too large");
        }

        return orderRepository.findAll(
                OrderSpecification.withFilter(filter),
                pageable
        );
    }

    private void processOrderItem(OrderEntity order, OrderItemRequest itemRequest) {
        ProductEntity product = getProduct(itemRequest.getProductId());

        product.reserve(itemRequest.getQuantity());

        OrderItemEntity orderItem = OrderItemEntity.builder()
                .product(product)
                .quantity(itemRequest.getQuantity())
                .price(product.getPrice())
                .build();

        order.addItem(orderItem);
    }

    private ProductEntity getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found")
                );
    }
}