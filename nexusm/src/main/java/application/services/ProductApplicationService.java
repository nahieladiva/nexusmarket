package application.services;

import application.domain.events.ProductAddedEvent;
import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Product;
import application.domain.models.User;
import application.domain.ports.in.ProductManagementPort;
import application.domain.ports.out.AuditLogPort;
import application.domain.ports.out.ProductRepository;
import application.domain.ports.out.UserRepository;
import application.domain.valueobjects.Money;
import application.domain.valueobjects.ProductCode;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de productos.
 */
@Service
public class ProductApplicationService implements ProductManagementPort {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuditLogPort auditLogPort;

    public ProductApplicationService(ProductRepository productRepository,
                                     UserRepository userRepository,
                                     AuditLogPort auditLogPort) {
        this.productRepository = Objects.requireNonNull(productRepository,
            "productRepository es obligatorio");
        this.userRepository = Objects.requireNonNull(userRepository,
            "userRepository es obligatorio");
        this.auditLogPort = Objects.requireNonNull(auditLogPort, "auditLogPort es obligatorio");
    }

    @Override
    public Product createProduct(ProductCode code, String name, String description,
                                 Money price, UserId sellerId) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor", sellerId.toString()));
        Product product = Product.create(code, name, description, price, seller.getId());
        Product saved = productRepository.save(product);
        auditLogPort.record(new ProductAddedEvent(
            saved.getId(), saved.getCode(), saved.getSellerId(), LocalDateTime.now()));
        return saved;
    }

    @Override
    public Product findProductById(ProductId id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", id.toString()));
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContaining(keyword.trim());
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void changePrice(ProductId id, Money newPrice) {
        Product product = findProductById(id);
        product.changePrice(newPrice);
        productRepository.save(product);
    }
}