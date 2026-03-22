package org.example.yourordercore.order.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.yourordercore.order.dto.OrderItemResponse;
import org.example.yourordercore.order.dto.OrderRequest;
import org.example.yourordercore.order.dto.OrderResponse;
import org.example.yourordercore.order.entity.OrderEntity;
import org.example.yourordercore.order.entity.OrderItemEntity;
import org.example.yourordercore.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        OrderEntity order = orderService.createOrder(request);
        return mapToResponse(order);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable UUID id) {
        OrderEntity order = orderService.getOrderById(id);
        return mapToResponse(order);
    }

    @PostMapping("/{id}/confirm")
    public OrderResponse confirmOrder(@PathVariable UUID id) {
        OrderEntity order = orderService.confirmOrder(id);
        return mapToResponse(order);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID id) {
        OrderEntity order = orderService.cancelOrder(id);
        return mapToResponse(order);
    }

    @PostMapping("/{id}/pay")
    public OrderResponse markOrderAsPaid(@PathVariable UUID id) {
        OrderEntity order = orderService.markOrderAsPaid(id);
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(OrderEntity order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .amount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    private OrderItemResponse mapToItemResponse(OrderItemEntity item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}
