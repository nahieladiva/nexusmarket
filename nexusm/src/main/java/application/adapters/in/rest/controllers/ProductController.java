package application.adapters.in.rest.controllers;

import application.adapters.in.rest.mappers.ProductMapper;
import application.adapters.in.rest.requests.CreateProductRequest;
import application.adapters.in.rest.requests.PriceChangeRequest;
import application.adapters.in.rest.responses.ProductResponse;
import application.domain.models.Product;
import application.domain.ports.in.ProductManagementPort;
import application.domain.valueobjects.ProductId;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los casos de uso de gestión de productos vía REST.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductManagementPort productManagementPort;
    private final ProductMapper productMapper;

    public ProductController(ProductManagementPort productManagementPort,
                             ProductMapper productMapper) {
        this.productManagementPort = productManagementPort;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody CreateProductRequest request) {
        Product domain = productMapper.toDomain(request);
        Product product = productManagementPort.createProduct(
            domain.getCode(), domain.getName(), domain.getDescription(),
            domain.getPrice(), domain.getSellerId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productMapper.toResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(productMapper.toResponse(
            productManagementPort.findProductById(ProductId.of(id))));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> search(
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(productManagementPort.searchProducts(keyword).stream()
            .map(productMapper::toResponse)
            .toList());
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponse> changePrice(
            @PathVariable String id, @RequestBody PriceChangeRequest request) {
        productManagementPort.changePrice(ProductId.of(id),
            productMapper.toDomain(request));
        return ResponseEntity.ok(productMapper.toResponse(
            productManagementPort.findProductById(ProductId.of(id))));
    }
}