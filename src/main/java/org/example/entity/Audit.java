package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;

import java.time.LocalDateTime;

/**
 * Represents an audit log entry capturing information about a user's actions.
 * Each audit entry includes a unique identifier, user login, audit type, and action type.
 *
 * @author ruslan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    /**
     * The unique identifier for the audit entry.
     */
    private Long id;

    /**
     * The username of the user associated with the audit entry.
     */
    private String username;

    /**
     * The type of audit, indicating the success or failure of an action.
     */
    private AuditType auditType;

    /**
     * The type of action performed by the user.
     */
    private ActionType actionType;

    /**
     * The type of action performed by the user.
     */
    private LocalDateTime auditTimestamp;
}
