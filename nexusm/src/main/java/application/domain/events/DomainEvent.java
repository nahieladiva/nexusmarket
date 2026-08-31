package application.domain.events;

import java.time.LocalDateTime;

/**
 * Marcador de eventos de dominio. Todo evento ocurre en un instante dado.
 */
public interface DomainEvent {

    LocalDateTime occurredAt();
}