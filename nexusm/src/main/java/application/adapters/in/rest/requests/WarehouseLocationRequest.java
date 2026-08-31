package application.adapters.in.rest.requests;

/**
 * DTO de ubicación dentro de un almacén.
 */
public record WarehouseLocationRequest(String aisle, String shelf, String bin) {
}