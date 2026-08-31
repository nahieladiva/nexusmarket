package application.domain.valueobjects;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object de correo electrónico.
 *
 * <p>Validaciones:</p>
 * <ul>
 *   <li>No puede ser nulo ni vacío.</li>
 *   <li>Debe cumplir el formato estándar de email.</li>
 *   <li>Se normaliza a minúsculas.</li>
 * </ul>
 */
public final class Email {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + value);
        }
        this.value = normalized;
    }

    public static Email of(String value) {
        return new Email(value);
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
        Email email = (Email) o;
        return value.equals(email.value);
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