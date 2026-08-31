package application.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object de código de producto (SKU).
 *
 * <p>Formato: 2 a 5 letras mayúsculas, guion y 4 a 8 dígitos, p. ej. {@code ELE-000123}.</p>
 */
public final class ProductCode {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]{2,5}-\\d{4,8}$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[A-Z]{2,5}$");

    private final String value;

    public ProductCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El código de producto (SKU) no puede ser nulo o vacío");
        }
        String normalized = value.trim().toUpperCase();
        if (!SKU_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                "Formato de SKU inválido: " + value + ". Formato esperado: XXX-0000");
        }
        this.value = normalized;
    }

    public static ProductCode of(String value) {
        return new ProductCode(value);
    }

    /**
     * Regla de generación: código de categoría + guion + secuencia.
     *
     * @param categoryCode 2 a 5 letras, p. ej. {@code ELEC}
     * @param sequence     número de secuencia
     */
    public static ProductCode generate(String categoryCode, int sequence) {
        if (categoryCode == null || !CATEGORY_PATTERN.matcher(categoryCode.toUpperCase()).matches()) {
            throw new IllegalArgumentException(
                "Código de categoría inválido: " + categoryCode + ". Formato esperado: 2-5 letras");
        }
        if (sequence < 0) {
            throw new IllegalArgumentException("La secuencia no puede ser negativa");
        }
        return new ProductCode(String.format("%s-%04d", categoryCode.toUpperCase(), sequence));
    }

    public String getValue() {
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
        ProductCode that = (ProductCode) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}