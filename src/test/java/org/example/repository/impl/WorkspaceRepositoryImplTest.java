package org.example.repository.impl;

import org.example.repository.WorkspaceRepository;
import org.example.entity.Workspace;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
public class WorkspaceRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private WorkspaceRepository workspaceRepository;
    private Connection connection;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
    private Workspace testWorkspace;

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

        workspaceRepository = new WorkspaceRepositoryImpl(connectionManager);

        testWorkspace = workspaceRepository.save(Workspace.builder()
                                                   .name("test-workspace-1")
                                                   .build());
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
    @DisplayName("Test findAll method")
    public void testFindAll(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();

        workspaceRepository.save(testWorkspace2);

        assertAll(
                () -> assertThat(workspaceRepository.findAll()).hasSize(2),
                () -> assertThat(workspaceRepository.findAll().isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("Test findById method")
    public void testFindById(){
        Optional<Workspace> foundWorkspace = workspaceRepository.findById(testWorkspace.getId());
        Optional<Workspace> notFoundWorkspace = workspaceRepository.findById(999L);
        assertAll(
                () -> assertThat(foundWorkspace.isPresent()).isTrue(),
                () -> assertThat(testWorkspace.getId()).isEqualTo(foundWorkspace.get().getId()),
                () -> assertThat(testWorkspace.getName()).isEqualTo(foundWorkspace.get().getName()),
                () -> assertThat(notFoundWorkspace.isPresent()).isFalse()
        );
    }

    @Test
    @DisplayName("Test deleteById method")
    public void testDeleteById(){
        assertAll(
                () -> assertThat(workspaceRepository.deleteById(testWorkspace.getId())).isTrue(),
                () -> assertThat(workspaceRepository.findById(testWorkspace.getId()).isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("Test deleteAll method")
    public void testDeleteAll(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();
        workspaceRepository.save(testWorkspace2);

        assertAll(
                () -> assertThat(workspaceRepository.deleteAll()).isTrue(),
                () -> assertThat(workspaceRepository.findAll().size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("Test save method")
    public void testSave(){
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();
        Workspace savedWorkspace = workspaceRepository.save(testWorkspace2);

        assertAll(
                () -> assertThat(workspaceRepository.findById(savedWorkspace.getId()).isPresent()).isTrue(),
                () -> assertThat(savedWorkspace.getId()).isEqualTo(testWorkspace2.getId()),
                () -> assertThat(savedWorkspace.getName()).isEqualTo(testWorkspace2.getName()),
                () -> assertThat(workspaceRepository.findAll().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("Test update method")
    public void testUpdate(){
        Workspace testWorkspace2= Workspace.builder()
                .name("test-workspace-2")
                .build();
        Workspace testWorkspaceUpdate = workspaceRepository.save(testWorkspace2);

        testWorkspaceUpdate.setName("test-workspace-update");
        workspaceRepository.update(testWorkspaceUpdate);

        assertAll(
                () -> assertThat(workspaceRepository.findById(testWorkspaceUpdate.getId())).isPresent(),
                () -> assertThat(workspaceRepository.findById(testWorkspaceUpdate.getId()).get().getName()).isEqualTo(testWorkspaceUpdate.getName())
        );
    }

    @Test
    @DisplayName("Test findByName method")
    public void testFindByName(){
        Optional<Workspace> foundWorkspace = workspaceRepository.findByName(testWorkspace.getName());
        Optional<Workspace> notFoundWorkspace = workspaceRepository.findByName(" ");

        assertAll(
                () -> assertThat(foundWorkspace.isPresent()).isTrue(),
                () -> assertThat(testWorkspace.getId()).isEqualTo(foundWorkspace.get().getId()),
                () -> assertThat(testWorkspace.getName()).isEqualTo(foundWorkspace.get().getName()),
                () -> assertThat(notFoundWorkspace.isPresent()).isFalse()
        );
    }

    @Test
    @DisplayName("Test deleteByName method")
    public void testDeleteByName(){
        assertAll(
                () -> assertThat(workspaceRepository.deleteByName(testWorkspace.getName())).isTrue(),
                () -> assertThat(workspaceRepository.findByName(testWorkspace.getName()).isEmpty()).isTrue()
        );
    }

}
