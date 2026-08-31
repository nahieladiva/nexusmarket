package application.services;

import application.domain.enums.OrderStatus;
import application.domain.events.InventoryUpdatedEvent;
import application.domain.events.OrderCreatedEvent;
import application.domain.events.OrderStatusChangedEvent;
import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Inventory;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.ports.in.OrderManagementPort;
import application.domain.ports.out.AuditLogPort;
import application.domain.ports.out.OrderRepository;
import application.domain.services.InventoryDomainService;
import application.domain.services.OrderFulfillmentDomainService;
import application.domain.services.WarehouseAllocationService;
import application.domain.valueobjects.OrderId;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.UserId;
import application.domain.valueobjects.WarehouseId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de órdenes.
 *
 * <p>Crea la orden, reserva el inventario en los almacenes asignados y publica
 * los eventos de dominio correspondientes.</p>
 */
@Service
public class OrderApplicationService implements OrderManagementPort {

    private final OrderRepository orderRepository;
    private final OrderFulfillmentDomainService orderFulfillment;
    private final WarehouseAllocationService warehouseAllocationService;
    private final InventoryDomainService inventoryDomainService;
    private final AuditLogPort auditLogPort;

    public OrderApplicationService(OrderRepository orderRepository,
                                   OrderFulfillmentDomainService orderFulfillment,
                                   WarehouseAllocationService warehouseAllocationService,
                                   InventoryDomainService inventoryDomainService,
                                   AuditLogPort auditLogPort) {
        this.orderRepository = Objects.requireNonNull(orderRepository,
            "orderRepository es obligatorio");
        this.orderFulfillment = Objects.requireNonNull(orderFulfillment,
            "orderFulfillment es obligatorio");
        this.warehouseAllocationService = Objects.requireNonNull(warehouseAllocationService,
            "warehouseAllocationService es obligatorio");
        this.inventoryDomainService = Objects.requireNonNull(inventoryDomainService,
            "inventoryDomainService es obligatorio");
        this.auditLogPort = Objects.requireNonNull(auditLogPort, "auditLogPort es obligatorio");
    }

    @Override
    public Order placeOrder(UserId buyerId, List<OrderItem> items) {
        // 1. Asignar almacenes y reservar stock (lanza excepción si no hay disponibilidad).
        Map<ProductId, WarehouseId> allocation = warehouseAllocationService.allocate(items);
        for (OrderItem item : items) {
            WarehouseId warehouseId = allocation.get(item.getProductId());
            Inventory inventory = inventoryDomainService.reserve(
                item.getProductId(), warehouseId, item.getQuantity());
            auditLogPort.record(new InventoryUpdatedEvent(
                item.getProductId(), warehouseId, inventory.getOnHand(), LocalDateTime.now()));
        }

        // 2. Crear la orden.
        Order order = orderFulfillment.placeOrder(buyerId, items);
        auditLogPort.record(new OrderCreatedEvent(
            order.getId(), order.getBuyerId(), order.getTotal(), LocalDateTime.now()));
        return order;
    }

    @Override
    public Order findOrderById(OrderId id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Orden", id.toString()));
    }

    @Override
    public List<Order> findOrdersByBuyer(UserId buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    @Override
    public Order confirmOrder(OrderId id) {
        return changeStatus(id, OrderStatus.CONFIRMED);
    }

    @Override
    public Order shipOrder(OrderId id) {
        return changeStatus(id, OrderStatus.SHIPPED);
    }

    @Override
    public Order deliverOrder(OrderId id) {
        return changeStatus(id, OrderStatus.DELIVERED);
    }

    @Override
    public Order cancelOrder(OrderId id) {
        return changeStatus(id, OrderStatus.CANCELLED);
    }

    private Order changeStatus(OrderId id, OrderStatus target) {
        Order order = findOrderById(id);
        OrderStatus previous = order.getStatus();
        Order updated;
        switch (target) {
            case CONFIRMED -> updated = orderFulfillment.confirmOrder(id);
            case SHIPPED -> updated = orderFulfillment.shipOrder(id);
            case DELIVERED -> updated = orderFulfillment.deliverOrder(id);
            case CANCELLED -> updated = orderFulfillment.cancelOrder(id);
            default -> throw new IllegalArgumentException("Estado no soportado: " + target);
        }
        auditLogPort.record(new OrderStatusChangedEvent(
            updated.getId(), previous, updated.getStatus(), LocalDateTime.now()));
        return updated;
    }
}