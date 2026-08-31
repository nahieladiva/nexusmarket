package application.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object de cantidad (inventario, unidades en una orden).
 *
 * <p>Validaciones:</p>
 * <ul>
 *   <li>Debe ser un número entero.</li>
 *   <li>No puede ser negativa.</li>
 *   <li>Las operaciones aritméticas nunca pueden producir valores negativos.</li>
 * </ul>
 */
public final class Quantity implements Comparable<Quantity> {

    private final int value;

    public Quantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + value);
        }
        this.value = value;
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

    public static Quantity zero() {
        return new Quantity(0);
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(other, "La cantidad a sumar no puede ser nula");
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        Objects.requireNonNull(other, "La cantidad a restar no puede ser nula");
        return new Quantity(this.value - other.value);
    }

    public int getValue() {
        return value;
    }

    public boolean isZero() {
        return value == 0;
    }

    @Override
    public int compareTo(Quantity other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}