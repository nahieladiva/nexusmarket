package application.adapters.in.rest.requests;

/**
 * DTO para crear un almacén.
 */
public record CreateWarehouseRequest(String name, AddressRequest address,
                                     WarehouseLocationRequest location) {
}