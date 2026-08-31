package application.adapters.in.rest.controllers;

import application.adapters.in.rest.responses.ErrorResponse;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.InsufficientStockException;
import application.domain.exceptions.OrderStateTransitionException;
import application.domain.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Traduce las excepciones del dominio y de validación a respuestas HTTP estandarizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                        HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler({InsufficientStockException.class, OrderStateTransitionException.class})
    public ResponseEntity<ErrorResponse> handleConflict(DomainException ex,
                                                        HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler({DomainException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex,
                                                          HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex,
                                                       HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocurrió un error interno en el servidor", request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message,
                                                HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(), status.value(), status.getReasonPhrase(),
            message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}