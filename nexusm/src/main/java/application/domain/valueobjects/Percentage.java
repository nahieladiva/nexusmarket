package application.domain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object de porcentaje (descuentos, impuestos, etc.).
 *
 * <p>Validaciones:</p>
 * <ul>
 *   <li>Debe estar en el rango [0, 100].</li>
 *   <li>Se normaliza a 2 decimales con redondeo {@code HALF_UP}.</li>
 * </ul>
 */
public final class Percentage {

    private final BigDecimal value;

    public Percentage(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("El porcentaje no puede ser nulo");
        }
        if (value.signum() < 0 || value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100: " + value);
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Percentage of(int value) {
        return new Percentage(BigDecimal.valueOf(value));
    }

    public static Percentage of(double value) {
        return new Percentage(BigDecimal.valueOf(value));
    }

    public static Percentage of(BigDecimal value) {
        return new Percentage(value);
    }

    /**
     * Porcentaje convertido a fracción, p. ej. {@code 15} -> {@code 0.15}.
     */
    public BigDecimal asFraction() {
        return value.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
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
        Percentage that = (Percentage) o;
        return value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toPlainString() + "%";
    }
}