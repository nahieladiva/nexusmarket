package application.adapters.in.rest.controllers;

import application.adapters.in.rest.mappers.InventoryMapper;
import application.adapters.in.rest.requests.AdjustInventoryRequest;
import application.adapters.in.rest.requests.CreateInventoryRequest;
import application.adapters.in.rest.responses.InventoryResponse;
import application.domain.ports.in.InventoryManagementPort;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los casos de uso de gestión de inventario vía REST.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryManagementPort inventoryManagementPort;
    private final InventoryMapper inventoryMapper;

    public InventoryController(InventoryManagementPort inventoryManagementPort,
                               InventoryMapper inventoryMapper) {
        this.inventoryManagementPort = inventoryManagementPort;
        this.inventoryMapper = inventoryMapper;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(
            @RequestBody CreateInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            inventoryMapper.toResponse(
                inventoryManagementPort.createInventory(
                    ProductId.of(request.productId()),
                    WarehouseId.of(request.warehouseId()),
                    Quantity.of(request.onHand()),
                    Quantity.of(request.reorderThreshold()),
                    inventoryMapper.toDomain(request.location()))));
    }

    @PatchMapping("/adjust")
    public ResponseEntity<InventoryResponse> adjust(
            @RequestBody AdjustInventoryRequest request) {
        return ResponseEntity.ok(inventoryMapper.toResponse(
            inventoryManagementPort.adjustStock(
                ProductId.of(request.productId()),
                WarehouseId.of(request.warehouseId()),
                Quantity.of(request.deltaQuantity()))));
    }

    @GetMapping
    public ResponseEntity<InventoryResponse> findByProductAndWarehouse(
            @RequestParam String productId, @RequestParam String warehouseId) {
        return ResponseEntity.ok(inventoryMapper.toResponse(
            inventoryManagementPort.findInventory(
                ProductId.of(productId), WarehouseId.of(warehouseId))));
    }

    @GetMapping("/warehouse")
    public ResponseEntity<List<InventoryResponse>> findByWarehouse(
            @RequestParam String warehouseId) {
        return ResponseEntity.ok(inventoryManagementPort.findByWarehouse(
            WarehouseId.of(warehouseId)).stream()
            .map(inventoryMapper::toResponse).toList());
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryResponse>> findAll() {
        return ResponseEntity.ok(inventoryManagementPort.findAllInventory().stream()
            .map(inventoryMapper::toResponse).toList());
    }
}