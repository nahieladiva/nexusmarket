package application.adapters.in.rest.mappers;

import application.adapters.in.rest.requests.CreateInventoryRequest;
import application.adapters.in.rest.requests.WarehouseLocationRequest;
import application.adapters.in.rest.responses.InventoryResponse;
import application.domain.models.Inventory;
import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;
import application.domain.valueobjects.WarehouseLocation;

import org.springframework.stereotype.Component;

/**
 * Mapea entre DTOs REST y el modelo de dominio de inventario.
 */
@Component
public class InventoryMapper {

    public Inventory toDomain(CreateInventoryRequest request) {
        return Inventory.create(
            ProductId.of(request.productId()),
            WarehouseId.of(request.warehouseId()),
            Quantity.of(request.onHand()),
            Quantity.of(request.reorderThreshold()),
            toDomain(request.location()));
    }

    public WarehouseLocation toDomain(WarehouseLocationRequest location) {
        return WarehouseLocation.of(location.aisle(), location.shelf(), location.bin());
    }

    public InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
            inventory.getId().toString(),
            inventory.getProductId().toString(),
            inventory.getWarehouseId().toString(),
            inventory.getOnHand().getValue(),
            inventory.getReorderThreshold().getValue(),
            inventory.getLocation().getAisle(),
            inventory.getLocation().getShelf(),
            inventory.getLocation().getBin());
    }
}