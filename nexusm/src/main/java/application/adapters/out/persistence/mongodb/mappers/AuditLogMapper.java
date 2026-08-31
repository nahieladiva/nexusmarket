package application.adapters.out.persistence.mongodb.mappers;

import application.adapters.out.persistence.mongodb.documents.AuditLogDocument;
import application.domain.events.DomainEvent;
import application.domain.events.InventoryUpdatedEvent;
import application.domain.events.LowStockEvent;
import application.domain.events.OrderCreatedEvent;
import application.domain.events.OrderStatusChangedEvent;
import application.domain.events.ProductAddedEvent;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Mapea eventos de dominio a documentos de auditoría de MongoDB.
 */
@Component
public class AuditLogMapper {

    private final ObjectMapper objectMapper;

    public AuditLogMapper(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper es obligatorio");
    }

    public AuditLogDocument toDocument(DomainEvent event) {
        return new AuditLogDocument(
            event.getClass().getSimpleName(),
            extractAggregateId(event),
            serialize(event),
            event.occurredAt());
    }

    private String extractAggregateId(DomainEvent event) {
        if (event instanceof OrderCreatedEvent created) {
            return created.orderId().toString();
        }
        if (event instanceof OrderStatusChangedEvent changed) {
            return changed.orderId().toString();
        }
        if (event instanceof InventoryUpdatedEvent updated) {
            return updated.productId() + "|" + updated.warehouseId();
        }
        if (event instanceof ProductAddedEvent added) {
            return added.productId().toString();
        }
        if (event instanceof LowStockEvent lowStock) {
            return lowStock.productId() + "|" + lowStock.warehouseId();
        }
        return event.getClass().getSimpleName();
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            return event.toString();
        }
    }
}