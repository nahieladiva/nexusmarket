package application.adapters.in.rest.controllers;

import application.adapters.in.rest.mappers.OrderMapper;
import application.adapters.in.rest.requests.CreateOrderRequest;
import application.adapters.in.rest.responses.OrderResponse;
import application.domain.models.Order;
import application.domain.ports.in.OrderManagementPort;
import application.domain.valueobjects.OrderId;
import application.domain.valueobjects.UserId;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los casos de uso de gestión de órdenes vía REST.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderManagementPort orderManagementPort;
    private final OrderMapper orderMapper;

    public OrderController(OrderManagementPort orderManagementPort, OrderMapper orderMapper) {
        this.orderManagementPort = orderManagementPort;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody CreateOrderRequest request) {
        Order order = orderManagementPort.placeOrder(
            UserId.of(request.buyerId()), orderMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderMapper.toResponse(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(orderMapper.toResponse(
            orderManagementPort.findOrderById(OrderId.of(id))));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findByBuyer(
            @RequestParam(required = false) String buyerId) {
        if (buyerId == null || buyerId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(orderManagementPort.findOrdersByBuyer(UserId.of(buyerId))
            .stream().map(orderMapper::toResponse).toList());
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable String id) {
        return ResponseEntity.ok(orderMapper.toResponse(
            orderManagementPort.confirmOrder(OrderId.of(id))));
    }

    @PatchMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> ship(@PathVariable String id) {
        return ResponseEntity.ok(orderMapper.toResponse(
            orderManagementPort.shipOrder(OrderId.of(id))));
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(@PathVariable String id) {
        return ResponseEntity.ok(orderMapper.toResponse(
            orderManagementPort.deliverOrder(OrderId.of(id))));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable String id) {
        return ResponseEntity.ok(orderMapper.toResponse(
            orderManagementPort.cancelOrder(OrderId.of(id))));
    }
}