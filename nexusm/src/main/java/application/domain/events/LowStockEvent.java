package application.domain.events;

import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento emitido cuando el inventario cae por debajo del punto de reorden.
 */
public record LowStockEvent(ProductId productId, WarehouseId warehouseId,
                            Quantity onHand, Quantity reorderThreshold,
                            LocalDateTime occurredAt) implements DomainEvent {

    public LowStockEvent {
        Objects.requireNonNull(productId, "productId es obligatorio");
        Objects.requireNonNull(warehouseId, "warehouseId es obligatorio");
        Objects.requireNonNull(onHand, "onHand es obligatorio");
        Objects.requireNonNull(reorderThreshold, "reorderThreshold es obligatorio");
        Objects.requireNonNull(occurredAt, "occurredAt es obligatorio");
    }
}