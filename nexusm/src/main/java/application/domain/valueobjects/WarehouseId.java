package application.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de almacén (UUID).
 */
public final class WarehouseId {

    private final UUID value;

    public WarehouseId(UUID value) {
        this.value = Objects.requireNonNull(value, "El id de almacén no puede ser nulo");
    }

    public static WarehouseId of(UUID value) {
        return new WarehouseId(value);
    }

    public static WarehouseId of(String value) {
        return new WarehouseId(UUID.fromString(value));
    }

    public static WarehouseId random() {
        return new WarehouseId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WarehouseId that = (WarehouseId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}