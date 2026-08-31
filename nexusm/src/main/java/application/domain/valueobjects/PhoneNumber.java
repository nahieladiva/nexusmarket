package application.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object de número de teléfono.
 *
 * <p>Validaciones:</p>
 * <ul>
 *   <li>Debe estar en formato internacional E.164 (p. ej. {@code +521234567890}).</li>
 *   <li>Los espacios y guiones se normalizan automáticamente.</li>
 * </ul>
 */
public final class PhoneNumber {

    // E.164: + seguido de 1 a 15 dígitos.
    private static final Pattern E164_PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");

    private final String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede ser nulo o vacío");
        }
        String normalized = value.trim().replaceAll("[\\s-]", "");
        if (!E164_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                "El teléfono debe estar en formato internacional E.164 (ej. +521234567890): " + value);
        }
        this.value = normalized;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
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
        PhoneNumber that = (PhoneNumber) o;
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