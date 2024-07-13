package org.example.service;

import org.example.repository.AuditRepository;
import org.example.entity.Audit;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    @DisplayName("Test saving record successfully")
    void testSaveAuditSuccess() {
        Audit audit = buildAudit("testUser", ActionType.REGISTRATION, AuditType.SUCCESS);

        when(auditRepository.save(audit)).thenReturn(audit);

        Audit savedAudit = auditService.save(audit);

        assertNotNull(savedAudit);
        assertEquals(audit, savedAudit);
        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    @DisplayName("Test retrieving all records successfully")
    void testGetAllAuditsSuccess() {
        Audit audit1 = buildAudit("user1", ActionType.REGISTRATION, AuditType.SUCCESS);

        Audit audit2 = buildAudit("testUser", ActionType.REGISTRATION, AuditType.FAIL);


        List<Audit> audits = Arrays.asList(audit1, audit2);

        when(auditRepository.findAll()).thenReturn(audits);

        List<Audit> retrievedAudits = auditService.getAllAudits();

        assertNotNull(retrievedAudits);
        assertEquals(2, retrievedAudits.size());
        assertEquals(audits, retrievedAudits);
        verify(auditRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test recorded for a specific action successfully")
    void testRecordAuditSuccess() {
        String username = "testUser";
        ActionType actionType = ActionType.REGISTRATION;
        AuditType auditType = AuditType.SUCCESS;

        Audit audit = buildAudit(username, actionType, auditType);


        when(auditRepository.save(any(Audit.class))).thenReturn(audit);

        Audit recordedAudit = auditService.record(username, actionType, auditType);

        assertNotNull(recordedAudit);
        assertEquals(username, recordedAudit.getUsername());
        assertEquals(actionType, recordedAudit.getActionType());
        assertEquals(auditType, recordedAudit.getAuditType());
        verify(auditRepository, times(1)).save(any(Audit.class));
    }

    public Audit buildAudit(String username, ActionType actionType, AuditType auditType){
        return Audit.builder()
                .username(username)
                .actionType(actionType)
                .auditType(auditType)
                .build();
    }
}