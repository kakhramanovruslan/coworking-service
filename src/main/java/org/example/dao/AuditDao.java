package org.example.dao;

import org.example.entity.Audit;

/**
 * The AuditDao interface provides methods for managing audit records in the database.
 * It inherits CRUD methods from the Dao interface.
 */
public interface AuditDao extends Dao<Long, Audit> {
}
