package application.domain.services;

import application.domain.exceptions.InsufficientStockException;
import application.domain.exceptions.ResourceNotFoundException;
import application.domain.models.Inventory;
import application.domain.models.OrderItem;
import application.domain.ports.out.InventoryRepository;
import application.domain.ports.out.WarehouseRepository;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Servicio de dominio para la asignación de almacenes a los items de una orden.
 */
public final class WarehouseAllocationService {

    private static final String NO_AVAILABLE_WAREHOUSE =
        "No hay almacén con stock disponible para el producto ";

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;

    public WarehouseAllocationService(InventoryRepository inventoryRepository,
                                      WarehouseRepository warehouseRepository) {
        this.inventoryRepository = Objects.requireNonNull(inventoryRepository,
            "inventoryRepository es obligatorio");
        this.warehouseRepository = Objects.requireNonNull(warehouseRepository,
            "warehouseRepository es obligatorio");
    }

    /**
     * Asigna un almacén a cada item de la orden. Retorna el mapa
     * {@code ProductId -> WarehouseId} con la asignación.
     *
     * <p>Estrategia: se priorizan los almacenes activos con inventario suficiente.</p>
     */
    public Map<ProductId, WarehouseId> allocate(List<OrderItem> items) {
        // Fuerza la carga de almacenes activos una sola vez.
        Map<WarehouseId, Boolean> activeWarehouses = new LinkedHashMap<>();
        warehouseRepository.findAll().forEach(warehouse ->
            activeWarehouses.put(warehouse.getId(), warehouse.isActive()));

        Map<ProductId, WarehouseId> allocation = new LinkedHashMap<>();
        for (OrderItem item : items) {
            allocation.put(item.getProductId(), allocateForProduct(item));
        }
        return allocation;
    }

    private WarehouseId allocateForProduct(OrderItem item) {
        List<Inventory> inventories = inventoryRepository.findByProductId(item.getProductId());
        Inventory candidate = inventories.stream()
            .filter(inv -> inv.isAvailable(item.getQuantity()))
            .findFirst()
            .orElseThrow(() -> new InsufficientStockException(
                item.getProductId(),
                null,
                item.getQuantity(),
                totalAvailable(item.getProductId(), inventories)));
        return candidate.getWarehouseId();
    }

    private Quantity totalAvailable(ProductId productId, List<Inventory> inventories) {
        int sum = inventories.stream().mapToInt(inv -> inv.getOnHand().getValue()).sum();
        return Quantity.of(sum);
    }
}