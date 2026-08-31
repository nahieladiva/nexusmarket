package application.domain.models;

import application.domain.enums.OrderStatus;
import application.domain.exceptions.OrderStateTransitionException;
import application.domain.valueobjects.Money;
import application.domain.valueobjects.OrderId;
import application.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root de la orden de compra.
 *
 * <p>Invariantes:</p>
 * <ul>
 *   <li>Una orden debe tener al menos un item.</li>
 *   <li>El total es siempre la suma de los subtotales de sus items.</li>
 *   <li>Los items solo se pueden agregar mientras la orden está en {@code PENDING}.</li>
 *   <li>Las transiciones de estado siguen la máquina de estados definida en
 *       {@link OrderStatus}; cualquier transición inválida lanza
 *       {@link OrderStateTransitionException}.</li>
 * </ul>
 */
public final class Order {

    private final OrderId id;
    private final UserId buyerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Money total;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(OrderId id, UserId buyerId, List<OrderItem> items, OrderStatus status,
                 Money total, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "El id de orden es obligatorio");
        this.buyerId = Objects.requireNonNull(buyerId, "El comprador es obligatorio");
        this.items = new ArrayList<>(items == null ? List.of() : items);
        if (this.items.isEmpty()) {
            throw new IllegalArgumentException("Una orden debe tener al menos un item");
        }
        this.status = status == null ? OrderStatus.PENDING : status;
        this.total = total != null ? total : calculateTotal();
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public static Order create(UserId buyerId, List<OrderItem> items) {
        LocalDateTime now = LocalDateTime.now();
        return new Order(OrderId.random(), buyerId, items, OrderStatus.PENDING,
            null, now, now);
    }

    public void addItem(OrderItem item) {
        if (status != OrderStatus.PENDING) {
            throw new OrderStateTransitionException(id, status, status);
        }
        this.items.add(Objects.requireNonNull(item, "El item es obligatorio"));
        this.total = calculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        transitionTo(OrderStatus.CONFIRMED);
    }

    public void ship() {
        transitionTo(OrderStatus.SHIPPED);
    }

    public void deliver() {
        transitionTo(OrderStatus.DELIVERED);
    }

    public void cancel() {
        transitionTo(OrderStatus.CANCELLED);
    }

    private void transitionTo(OrderStatus target) {
        if (!this.status.canTransitionTo(target)) {
            throw new OrderStateTransitionException(id, this.status, target);
        }
        this.status = target;
        this.updatedAt = LocalDateTime.now();
    }

    private Money calculateTotal() {
        Money result = null;
        for (OrderItem item : items) {
            result = (result == null) ? item.subtotal() : result.add(item.subtotal());
        }
        return result;
    }

    public OrderId getId() {
        return id;
    }

    public UserId getBuyerId() {
        return buyerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}