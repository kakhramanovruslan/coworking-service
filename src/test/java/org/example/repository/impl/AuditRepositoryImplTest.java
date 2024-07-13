package org.example.repository.impl;

import org.example.repository.AuditRepository;
import org.example.entity.Audit;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AuditRepositoryImplTest {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private AuditRepository auditRepository;
    private Connection connection;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();

    @BeforeAll
    public static void setUpAll() {
        postgresContainer.start();
    }

    @BeforeEach
    public void setUp(){
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword(),
                postgresContainer.getDriverClassName());

        liquibaseManager.runMigrations(connection);

        auditRepository = new AuditRepositoryImpl(connectionManager);
    }

    @AfterEach
    public void reset(){
        liquibaseManager.rollbackToCreateTables(connection);
    }

    @AfterAll
    public static void resetAll(){
        postgresContainer.stop();
    }

    @Test
    @DisplayName("Save and find by ID")
    public void testSaveAndFindById() {
        Audit auditToSave = buildAudit("testUser", ActionType.AUTHORIZATION, AuditType.SUCCESS);

        Audit savedAudit = auditRepository.save(auditToSave);

        Optional<Audit> foundAudit = auditRepository.findById(savedAudit.getId());

        assertTrue(foundAudit.isPresent());
        assertEquals(savedAudit.getId(), foundAudit.get().getId());
        assertEquals(savedAudit.getUsername(), foundAudit.get().getUsername());
        assertEquals(savedAudit.getActionType(), foundAudit.get().getActionType());
        assertEquals(savedAudit.getAuditType(), foundAudit.get().getAuditType());
    }

    @Test
    @DisplayName("Find all records")
    public void testFindAll() {
        Audit audit1 = buildAudit("testUser1", ActionType.AUTHORIZATION, AuditType.SUCCESS);
        Audit audit2 = buildAudit("testUser2", ActionType.AUTHORIZATION, AuditType.SUCCESS);

        auditRepository.save(audit1);
        auditRepository.save(audit2);

        List<Audit> allAudits = auditRepository.findAll();

        assertEquals(2, allAudits.size());
    }

    @Test
    @DisplayName("Update record")
    public void testUpdate() {
        Audit auditToSave = buildAudit("testUser", ActionType.REGISTRATION, AuditType.SUCCESS);
        Audit savedAudit = auditRepository.save(auditToSave);

        savedAudit.setUsername("updatedUser");
        savedAudit.setActionType(ActionType.DELETE_WORKSPACE);
        savedAudit.setAuditType(AuditType.FAIL);

        assertTrue(auditRepository.update(savedAudit));

        Optional<Audit> updatedAudit = auditRepository.findById(savedAudit.getId());

        assertTrue(updatedAudit.isPresent());
        assertEquals("updatedUser", updatedAudit.get().getUsername());
        assertEquals(ActionType.DELETE_WORKSPACE, updatedAudit.get().getActionType());
        assertEquals(AuditType.FAIL, updatedAudit.get().getAuditType());
    }

    @Test
    @DisplayName("Delete record by ID")
    public void testDeleteById() {
        Audit auditToSave = buildAudit("testUser", ActionType.REGISTRATION, AuditType.SUCCESS);
        Audit savedAudit = auditRepository.save(auditToSave);

        assertTrue(auditRepository.deleteById(savedAudit.getId()));

        Optional<Audit> deletedAudit = auditRepository.findById(savedAudit.getId());

        assertFalse(deletedAudit.isPresent());
    }



    public Audit buildAudit(String username, ActionType actionType, AuditType auditType){
        return Audit.builder()
                .username(username)
                .actionType(actionType)
                .auditType(auditType)
                .build();
    }

}
