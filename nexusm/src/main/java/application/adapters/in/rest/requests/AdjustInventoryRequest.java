package application.adapters.in.rest.requests;

/**
 * DTO para ajustar stock: {@code deltaQuantity} positivo incrementa, negativo decrementa.
 */
public record AdjustInventoryRequest(String productId, String warehouseId,
                                     int deltaQuantity) {
}