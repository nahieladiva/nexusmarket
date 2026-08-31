package application.adapters.in.rest.requests;

/**
 * DTO de item dentro de una orden.
 */
public record OrderItemRequest(String productId, int quantity, PriceRequest unitPrice) {
}