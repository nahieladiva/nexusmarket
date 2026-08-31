package application.domain.ports.in;

import application.domain.models.Product;
import application.domain.valueobjects.Money;
import application.domain.valueobjects.ProductCode;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.UserId;

import java.util.List;

/**
 * Puerto de entrada (casos de uso) de gestión de productos.
 */
public interface ProductManagementPort {

    Product createProduct(ProductCode code, String name, String description,
                          Money price, UserId sellerId);

    Product findProductById(ProductId id);

    List<Product> searchProducts(String keyword);

    List<Product> findAllProducts();

    void changePrice(ProductId id, Money newPrice);
}