package application.adapters.out.persistence.mongodb.adapters;

import application.adapters.out.persistence.mongodb.mappers.AuditLogMapper;
import application.adapters.out.persistence.mongodb.repositories.AuditLogMongoRepository;
import application.domain.events.DomainEvent;
import application.domain.ports.out.AuditLogPort;

import java.util.Objects;

import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que implementa {@link AuditLogPort} sobre MongoDB.
 */
@Component
public class AuditLogPersistenceAdapter implements AuditLogPort {

    private final AuditLogMongoRepository repository;
    private final AuditLogMapper mapper;

    public AuditLogPersistenceAdapter(AuditLogMongoRepository repository,
                                      AuditLogMapper mapper) {
        this.repository = Objects.requireNonNull(repository, "repository es obligatorio");
        this.mapper = Objects.requireNonNull(mapper, "mapper es obligatorio");
    }

    @Override
    public void record(DomainEvent event) {
        repository.save(mapper.toDocument(event));
    }
}