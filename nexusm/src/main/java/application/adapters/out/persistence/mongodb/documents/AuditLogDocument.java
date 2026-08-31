package application.adapters.out.persistence.mongodb.documents;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento de MongoDB para la auditoría de eventos de dominio.
 */
@Document(collection = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogDocument {

    @Id
    private String id;

    private String eventType;

    private String aggregateId;

    private String payload;

    private LocalDateTime occurredAt;

    public AuditLogDocument(String eventType, String aggregateId,
                            String payload, LocalDateTime occurredAt) {
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.occurredAt = occurredAt;
    }
}