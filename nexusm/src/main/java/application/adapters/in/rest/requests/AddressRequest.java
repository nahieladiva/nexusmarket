package application.adapters.in.rest.requests;

/**
 * DTO de dirección para transportar datos de entrada/salida REST.
 */
public record AddressRequest(String street, String city, String state,
                             String zipCode, String country) {
}