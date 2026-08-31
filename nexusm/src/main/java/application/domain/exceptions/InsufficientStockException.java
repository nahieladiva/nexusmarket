package application.domain.exceptions;

import application.domain.valueobjects.ProductId;
import application.domain.valueobjects.Quantity;
import application.domain.valueobjects.WarehouseId;

/**
 * Se lanza cuando no hay suficiente inventario disponible para satisfacer
 * una cantidad solicitada de un producto. El {@code warehouseId} puede ser
 * {@code null} cuando la escasez se evalúa de forma agregada.
 */
public class InsufficientStockException extends DomainException {

    private final ProductId productId;
    private final WarehouseId warehouseId;
    private final int requested;
    private final int available;

    public InsufficientStockException(ProductId productId, WarehouseId warehouseId,
                                      Quantity requested, Quantity available) {
        super("Stock insuficiente del producto " + productId
            + (warehouseId != null ? " en el almacén " + warehouseId : "")
            + ": solicitado " + requested.getValue()
            + ", disponible " + available.getValue());
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.requested = requested.getValue();
        this.available = available.getValue();
    }

    public ProductId getProductId() {
        return productId;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}