package application.adapters.in.rest.responses;

/**
 * DTO de inventario para respuestas REST.
 */
public record InventoryResponse(String id, String productId, String warehouseId,
                                int onHand, int reorderThreshold,
                                String aisle, String shelf, String bin) {
}