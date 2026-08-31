package application.adapters.in.rest.requests;

/**
 * DTO para crear un registro de inventario.
 */
public record CreateInventoryRequest(String productId, String warehouseId,
                                     int onHand, int reorderThreshold,
                                     WarehouseLocationRequest location) {
}