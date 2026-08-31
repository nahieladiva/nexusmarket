package application.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import application.domain.enums.OrderStatus;
import application.domain.exceptions.OrderStateTransitionException;
import application.domain.valueobjects.Money;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.UserId;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de la entidad {@link Order}.
 */
class OrderTest {

    private OrderItem item(String price) {
        return new OrderItem(ProductId.random(), Quantity.of(1), Money.of(price, "USD"));
    }

    @Test
    void totalIsSumOfSubtotals() {
        OrderItem item1 = new OrderItem(ProductId.random(), Quantity.of(2), Money.of("10.00", "USD"));
        OrderItem item2 = new OrderItem(ProductId.random(), Quantity.of(1), Money.of("5.00", "USD"));
        Order order = Order.create(UserId.random(), List.of(item1, item2));
        assertEquals("25.00", order.getTotal().getAmount().toPlainString());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void orderRequiresAtLeastOneItem() {
        assertThrows(IllegalArgumentException.class,
            () -> Order.create(UserId.random(), List.of()));
    }

    @Test
    void followsValidLifecycle() {
        Order order = Order.create(UserId.random(), List.of(item("1.00")));
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void rejectsInvalidTransition() {
        Order order = Order.create(UserId.random(), List.of(item("1.00")));
        assertThrows(OrderStateTransitionException.class, order::ship);
        assertThrows(OrderStateTransitionException.class, order::deliver);
    }

    @Test
    void doesNotAllowAddingItemsAfterConfirmation() {
        Order order = Order.create(UserId.random(), List.of(item("1.00")));
        order.confirm();
        assertThrows(OrderStateTransitionException.class,
            () -> order.addItem(item("2.00")));
    }

    @Test
    void cancelledOrderIsTerminal() {
        Order order = Order.create(UserId.random(), List.of(item("1.00")));
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertThrows(OrderStateTransitionException.class, order::confirm);
    }
}