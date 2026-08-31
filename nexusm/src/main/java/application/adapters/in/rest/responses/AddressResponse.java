package application.adapters.in.rest.responses;

/**
 * DTO de dirección para respuestas REST.
 */
public record AddressResponse(String street, String city, String state,
                              String zipCode, String country) {
}