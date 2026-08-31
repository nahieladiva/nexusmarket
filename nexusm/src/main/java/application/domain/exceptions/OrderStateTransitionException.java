package application.domain.exceptions;

import application.domain.enums.OrderStatus;
import application.domain.valueobjects.OrderId;

/**
 * Se lanza cuando se intenta ejecutar una transición de estado no permitida
 * sobre una orden.
 */
public class OrderStateTransitionException extends DomainException {

    private final OrderId orderId;
    private final OrderStatus current;
    private final OrderStatus target;

    public OrderStateTransitionException(OrderId orderId, OrderStatus current, OrderStatus target) {
        super("Transición de estado de la orden " + orderId
            + " no permitida: " + current + " -> " + target);
        this.orderId = orderId;
        this.current = current;
        this.target = target;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrent() {
        return current;
    }

    public OrderStatus getTarget() {
        return target;
    }
}