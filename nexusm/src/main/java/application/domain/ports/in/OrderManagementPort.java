package application.domain.ports.in;

import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.valueobjects.OrderId;
import application.domain.valueobjects.UserId;

import java.util.List;

/**
 * Puerto de entrada (casos de uso) de gestión de órdenes.
 */
public interface OrderManagementPort {

    Order placeOrder(UserId buyerId, List<OrderItem> items);

    Order findOrderById(OrderId id);

    List<Order> findOrdersByBuyer(UserId buyerId);

    Order confirmOrder(OrderId id);

    Order shipOrder(OrderId id);

    Order deliverOrder(OrderId id);

    Order cancelOrder(OrderId id);
}