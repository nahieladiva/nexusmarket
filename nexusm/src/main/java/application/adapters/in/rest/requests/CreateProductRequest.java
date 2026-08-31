package application.adapters.in.rest.requests;

/**
 * DTO para publicar un producto.
 */
public record CreateProductRequest(String code, String name, String description,
                                   PriceRequest price, String sellerId) {
}