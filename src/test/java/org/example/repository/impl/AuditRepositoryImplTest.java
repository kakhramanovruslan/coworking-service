package org.example.dao.impl;

import org.example.dao.AuditDao;
import org.example.entity.Audit;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuditDaoImplTest {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private AuditDao auditDao;
    private ConnectionManager connectionManager;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();

    @BeforeAll
    public static void setUpAll() {
        postgresContainer.start();
    }

    @BeforeEach
    public void setUp(){
        connectionManager = new ConnectionManager(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword(),
                postgresContainer.getDriverClassName());

        liquibaseManager.runMigrations(connectionManager.getConnection());

        auditDao = new AuditDaoImpl(connectionManager);
    }

    @AfterEach
    public void reset(){
        liquibaseManager.rollbackToCreateTables(connectionManager.getConnection());
    }

    @AfterAll
    public static void resetAll(){
        postgresContainer.stop();
    }

    @Test
    @DisplayName("Save and find by ID")
    public void testSaveAndFindById() {
        Audit auditToSave = buildAudit("testUser", ActionType.AUTHORIZATION, AuditType.SUCCESS);

        Audit savedAudit = auditDao.save(auditToSave);

        Optional<Audit> foundAudit = auditDao.findById(savedAudit.getId());

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

        auditDao.save(audit1);
        auditDao.save(audit2);

        List<Audit> allAudits = auditDao.findAll();

        assertEquals(2, allAudits.size());
    }

    @Test
    @DisplayName("Update record")
    public void testUpdate() {
        Audit auditToSave = buildAudit("testUser", ActionType.REGISTRATION, AuditType.SUCCESS);
        Audit savedAudit = auditDao.save(auditToSave);

        savedAudit.setUsername("updatedUser");
        savedAudit.setActionType(ActionType.DELETE_WORKSPACE);
        savedAudit.setAuditType(AuditType.FAIL);

        assertTrue(auditDao.update(savedAudit));

        Optional<Audit> updatedAudit = auditDao.findById(savedAudit.getId());

        assertTrue(updatedAudit.isPresent());
        assertEquals("updatedUser", updatedAudit.get().getUsername());
        assertEquals(ActionType.DELETE_WORKSPACE, updatedAudit.get().getActionType());
        assertEquals(AuditType.FAIL, updatedAudit.get().getAuditType());
    }

    @Test
    @DisplayName("Delete record by ID")
    public void testDeleteById() {
        Audit auditToSave = buildAudit("testUser", ActionType.REGISTRATION, AuditType.SUCCESS);
        Audit savedAudit = auditDao.save(auditToSave);

        assertTrue(auditDao.deleteById(savedAudit.getId()));

        Optional<Audit> deletedAudit = auditDao.findById(savedAudit.getId());

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
