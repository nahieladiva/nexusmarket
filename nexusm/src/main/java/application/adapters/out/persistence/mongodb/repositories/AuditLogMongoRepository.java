package application.adapters.out.persistence.mongodb.repositories;

import application.adapters.out.persistence.mongodb.documents.AuditLogDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio de MongoDB para documentos de auditoría.
 */
public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
}