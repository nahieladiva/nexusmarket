package application.adapters.in.rest.mappers;

import application.adapters.in.rest.requests.CreateProductRequest;
import application.adapters.in.rest.requests.PriceChangeRequest;
import application.adapters.in.rest.responses.ProductResponse;
import application.domain.models.Product;
import application.domain.valueobjects.Money;
import application.domain.valueobjects.ProductCode;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.UserId;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

/**
 * Mapea entre DTOs REST y el modelo de dominio de productos.
 */
@Component
public class ProductMapper {

    public Product toDomain(CreateProductRequest request) {
        return Product.create(
            ProductCode.of(request.code()),
            request.name(),
            request.description(),
            Money.of(request.price().amount(), request.price().currency()),
            UserId.of(request.sellerId()));
    }

    public Money toDomain(PriceChangeRequest request) {
        return toDomain(request.price());
    }

    public Money toDomain(application.adapters.in.rest.requests.PriceRequest price) {
        return new Money(new BigDecimal(price.amount()),
            java.util.Currency.getInstance(price.currency()));
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId().toString(),
            product.getCode().getValue(),
            product.getName(),
            product.getDescription(),
            product.getPrice().getAmount().toPlainString(),
            product.getPrice().getCurrency().getCurrencyCode(),
            product.getSellerId().toString(),
            product.isActive(),
            product.getCreatedAt());
    }
}