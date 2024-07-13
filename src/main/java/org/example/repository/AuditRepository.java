package org.example.repository;

import org.example.entity.Audit;

/**
 * The AuditRepository interface provides methods for managing audit records in the database.
 * It inherits CRUD methods from the Repository interface.
 */
public interface AuditRepository extends Repository<Long, Audit> {
}
