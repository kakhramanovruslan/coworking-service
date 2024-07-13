package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.AuditRepository;
import org.example.entity.Audit;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing audits.
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     * @return the saved audit record
     */
    public Audit save(Audit audit) {
        return auditRepository.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    public List<Audit> getAllAudits() {
        return auditRepository.findAll();
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
