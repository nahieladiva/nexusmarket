package application.domain.ports.in;

import application.domain.models.Inventory;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;
import application.domain.valueobjects.WarehouseLocation;

import java.util.List;

/**
 * Puerto de entrada (casos de uso) de gestión de inventario.
 */
public interface InventoryManagementPort {

    Inventory createInventory(ProductId productId, WarehouseId warehouseId,
                              Quantity onHand, Quantity reorderThreshold,
                              WarehouseLocation location);

    /**
     * Ajusta el stock: {@code delta} positivo incrementa, negativo decrementa.
     */
    Inventory adjustStock(ProductId productId, WarehouseId warehouseId, Quantity delta);

    Inventory findInventory(ProductId productId, WarehouseId warehouseId);

    boolean isAvailable(ProductId productId, WarehouseId warehouseId, Quantity requested);

    List<Inventory> findByWarehouse(WarehouseId warehouseId);

    List<Inventory> findAllInventory();
}