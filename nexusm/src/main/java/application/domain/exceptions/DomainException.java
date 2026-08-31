package application.domain.exceptions;

/**
 * Excepción base del dominio. Todas las reglas de negocio violadas
 * se representan mediante subclases de esta excepción.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}