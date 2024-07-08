package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.AuditDao;
import org.example.entity.Audit;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;

import java.util.List;

/**
 * Service class for managing audits.
 */
@RequiredArgsConstructor
public class AuditService {

    private final AuditDao auditDao;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     * @return the saved audit record
     */
    public Audit save(Audit audit) {
        return auditDao.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    public List<Audit> getAllAudits() {
        return auditDao.findAll();
    }

    /**
     * Performs an audit for a specific action.
     *
     * @param username      the login associated with the action
     * @param actionType the type of action
     * @param auditType  the type of audit (SUCCESS or FAIL)
     * @return
     */
    public Audit record(String username, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .username(username)
                .actionType(actionType)
                .auditType(auditType)
                .build();

        return save(audit);
    }
}
