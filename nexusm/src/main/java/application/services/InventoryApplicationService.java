package application.services;

import application.domain.events.InventoryUpdatedEvent;
import application.domain.events.LowStockEvent;
import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Inventory;
import application.domain.ports.in.InventoryManagementPort;
import application.domain.ports.out.AuditLogPort;
import application.domain.ports.out.InventoryRepository;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;
import application.domain.valueobjects.WarehouseLocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación que implementa los casos de uso de gestión de inventario.
 */
@Service
public class InventoryApplicationService implements InventoryManagementPort {

    private final InventoryRepository inventoryRepository;
    private final AuditLogPort auditLogPort;

    public InventoryApplicationService(InventoryRepository inventoryRepository,
                                       AuditLogPort auditLogPort) {
        this.inventoryRepository = Objects.requireNonNull(inventoryRepository,
            "inventoryRepository es obligatorio");
        this.auditLogPort = Objects.requireNonNull(auditLogPort, "auditLogPort es obligatorio");
    }

    @Override
    public Inventory createInventory(ProductId productId, WarehouseId warehouseId,
                                     Quantity onHand, Quantity reorderThreshold,
                                     WarehouseLocation location) {
        Inventory inventory = Inventory.create(
            productId, warehouseId, onHand, reorderThreshold, location);
        Inventory saved = inventoryRepository.save(inventory);
        publishInventoryEvents(saved);
        return saved;
    }

    @Override
    public Inventory adjustStock(ProductId productId, WarehouseId warehouseId, Quantity delta) {
        Inventory inventory = findInventory(productId, warehouseId);
        if (delta.getValue() >= 0) {
            inventory.increase(delta);
        } else {
            // El delta es negativo: se decrementa con su magnitud (valida disponibilidad).
            inventory.decrease(Quantity.of(-delta.getValue()));
        }
        Inventory saved = inventoryRepository.save(inventory);
        publishInventoryEvents(saved);
        return saved;
    }

    @Override
    public Inventory findInventory(ProductId productId, WarehouseId warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Inventario", productId + "|" + warehouseId));
    }

    @Override
    public boolean isAvailable(ProductId productId, WarehouseId warehouseId, Quantity requested) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
            .map(inv -> inv.isAvailable(requested))
            .orElse(false);
    }

    @Override
    public List<Inventory> findByWarehouse(WarehouseId warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public List<Inventory> findAllInventory() {
        return inventoryRepository.findAll();
    }

    private void publishInventoryEvents(Inventory inventory) {
        LocalDateTime now = LocalDateTime.now();
        auditLogPort.record(new InventoryUpdatedEvent(
            inventory.getProductId(), inventory.getWarehouseId(),
            inventory.getOnHand(), now));
        if (inventory.isBelowReorderPoint()) {
            auditLogPort.record(new LowStockEvent(
                inventory.getProductId(), inventory.getWarehouseId(),
                inventory.getOnHand(), inventory.getReorderThreshold(), now));
        }
    }
}