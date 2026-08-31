package application.adapters.in.rest.responses;

/**
 * DTO de item de orden para respuestas REST.
 */
public record OrderItemResponse(String productId, int quantity,
                                String unitPriceAmount, String unitPriceCurrency,
                                String subtotalAmount) {
}