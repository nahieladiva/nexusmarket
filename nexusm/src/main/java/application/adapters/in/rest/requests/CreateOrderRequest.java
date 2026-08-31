package application.adapters.in.rest.requests;

import java.util.List;

/**
 * DTO para crear una orden.
 */
public record CreateOrderRequest(String buyerId, List<OrderItemRequest> items) {
}