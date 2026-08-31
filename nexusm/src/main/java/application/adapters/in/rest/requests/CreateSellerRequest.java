package application.adapters.in.rest.requests;

/**
 * DTO para registrar un vendedor.
 */
public record CreateSellerRequest(String fullName, String email, String phone,
                                  String passwordHash, String businessName) {
}