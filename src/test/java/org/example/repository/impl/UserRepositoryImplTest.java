package org.example.repository.impl;

import org.example.repository.UserRepository;
import org.example.entity.User;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private UserRepository userRepository;
    private Connection connection;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
    private User testUser;

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

        userRepository = new UserRepositoryImpl(connectionManager);

        userRepository.deleteById(-1L); // delete admin

        testUser = userRepository.save(User.builder()
                                    .username("ruslan")
                                    .password("123")
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
    @DisplayName("Test findById method")
    public void testFindById(){
        Optional<User> foundUser = userRepository.findById(testUser.getId());
        Optional<User> notFoundPlayer = userRepository.findById(999L);

        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(testUser.getId(), foundUser.get().getId()),
                () -> assertEquals(testUser.getUsername(), foundUser.get().getUsername()),
                () -> assertEquals(testUser.getPassword(), foundUser.get().getPassword()),
                () -> assertFalse(notFoundPlayer.isPresent())
        );
    }

    @Test
    @DisplayName("Test deleteById method")
    public void testDeleteById(){
        assertTrue(userRepository.deleteById(testUser.getId()));
        assertTrue(userRepository.findById(testUser.getId()).isEmpty());
    }

    @Test
    @DisplayName("Test deleteAll method")
    public void testDeleteAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userRepository.save(testUser2);

        assertTrue(userRepository.deleteAll());
        assertEquals(userRepository.findAll().size(), 0);
    }

    @Test
    @DisplayName("Test findAll method")
    public void testFindAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userRepository.save(testUser2);

        assertEquals(userRepository.findAll().size(), 2);
        assertFalse(userRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Test save method")
    public void testSave(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        User savedUser = userRepository.save(testUser2);

        assertAll(
                () -> assertTrue(userRepository.findById(savedUser.getId()).isPresent()),
                () -> assertEquals(savedUser.getId(), testUser2.getId()),
                () -> assertEquals(savedUser.getUsername(), testUser2.getUsername()),
                () -> assertEquals(savedUser.getPassword(), testUser2.getPassword()),
                () -> assertEquals(userRepository.findAll().size(), 2)
        );
    }

    @Test
    @DisplayName("Test update method")
    @Disabled("Not implemented yet")
    public void testUpdate(){
    }

    @Test
    @DisplayName("Test findByUsername method")
    public void testFindByUsername(){
        Optional<User> foundUser = userRepository.findByUsername(testUser.getUsername());
        Optional<User> notFoundPlayer = userRepository.findByUsername(" ");

        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(testUser.getId(), foundUser.get().getId()),
                () -> assertEquals(testUser.getUsername(), foundUser.get().getUsername()),
                () -> assertEquals(testUser.getPassword(), foundUser.get().getPassword()),
                () -> assertFalse(notFoundPlayer.isPresent())
        );
    }
}
