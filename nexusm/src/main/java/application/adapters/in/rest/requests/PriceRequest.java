package application.adapters.in.rest.requests;

/**
 * DTO de precio (monto + moneda) para transporte REST.
 */
public record PriceRequest(String amount, String currency) {
}