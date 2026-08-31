package application.domain.services;

import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.ports.out.OrderRepository;
import application.domain.valueobjects.OrderId;
import application.domain.valueobjects.UserId;

import java.util.List;
import java.util.Objects;

/**
 * Servicio de dominio para el cumplimiento de órdenes (creación y transiciones de estado).
 */
public final class OrderFulfillmentDomainService {

    private final OrderRepository orderRepository;
    private final PricingDomainService pricingDomainService;

    public OrderFulfillmentDomainService(OrderRepository orderRepository,
                                         PricingDomainService pricingDomainService) {
        this.orderRepository = Objects.requireNonNull(orderRepository,
            "orderRepository es obligatorio");
        this.pricingDomainService = Objects.requireNonNull(pricingDomainService,
            "pricingDomainService es obligatorio");
    }

    /**
     * Crea una orden en estado {@code PENDING} con su total calculado.
     */
    public Order placeOrder(UserId buyerId, List<OrderItem> items) {
        pricingDomainService.calculateOrderTotal(items); // valida items y moneda
        return orderRepository.save(Order.create(buyerId, items));
    }

    public Order confirmOrder(OrderId orderId) {
        Order order = findOrThrow(orderId);
        order.confirm();
        return orderRepository.save(order);
    }

    public Order shipOrder(OrderId orderId) {
        Order order = findOrThrow(orderId);
        order.ship();
        return orderRepository.save(order);
    }

    public Order deliverOrder(OrderId orderId) {
        Order order = findOrThrow(orderId);
        order.deliver();
        return orderRepository.save(order);
    }

    public Order cancelOrder(OrderId orderId) {
        Order order = findOrThrow(orderId);
        order.cancel();
        return orderRepository.save(order);
    }

    private Order findOrThrow(OrderId orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Orden", orderId.toString()));
    }
}