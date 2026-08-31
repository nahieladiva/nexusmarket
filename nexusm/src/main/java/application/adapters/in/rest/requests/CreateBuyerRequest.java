package application.adapters.in.rest.requests;

/**
 * DTO para registrar un comprador.
 */
public record CreateBuyerRequest(String fullName, String email, String phone,
                                 String passwordHash, AddressRequest defaultShippingAddress) {
}