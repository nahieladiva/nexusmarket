package application.domain.ports.out;

import application.domain.events.DomainEvent;

/**
 * Puerto de salida para la persistencia de auditoría de eventos de dominio.
 * Implementado por el adaptador MongoDB.
 */
public interface AuditLogPort {

    void record(DomainEvent event);
}