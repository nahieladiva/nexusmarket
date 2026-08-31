package application.adapters.in.rest.responses;

import java.time.LocalDateTime;

/**
 * DTO estandarizado de error para respuestas REST.
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String error,
                            String message, String path) {
}