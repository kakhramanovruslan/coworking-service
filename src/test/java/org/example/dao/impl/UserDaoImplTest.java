package org.example.dao.impl;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private UserDao userDao;
    private ConnectionManager connectionManager;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
    private User testUser;

    @BeforeAll
    public static void setUpAll() {
        postgresContainer.start();
    }

    @BeforeEach
    public void setUp(){
        connectionManager = new ConnectionManager(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        liquibaseManager.runMigrations(connectionManager.getConnection());

        userDao = new UserDaoImpl(connectionManager);

        userDao.deleteById(-1L); // delete admin

        testUser = userDao.save(User.builder()
                                    .username("ruslan")
                                    .password("123")
                                    .build());
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
    @DisplayName("Test findById method")
    public void testFindById(){
        Optional<User> foundUser = userDao.findById(testUser.getId());
        Optional<User> notFoundPlayer = userDao.findById(999L);

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
        assertTrue(userDao.deleteById(testUser.getId()));
        assertTrue(userDao.findById(testUser.getId()).isEmpty());
    }

    @Test
    @DisplayName("Test deleteAll method")
    public void testDeleteAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userDao.save(testUser2);

        assertTrue(userDao.deleteAll());
        assertEquals(userDao.findAll().size(), 0);
    }

    @Test
    @DisplayName("Test findAll method")
    public void testFindAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userDao.save(testUser2);

        assertEquals(userDao.findAll().size(), 2);
        assertFalse(userDao.findAll().isEmpty());
    }

    @Test
    @DisplayName("Test save method")
    public void testSave(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        User savedUser = userDao.save(testUser2);

        assertAll(
                () -> assertTrue(userDao.findById(savedUser.getId()).isPresent()),
                () -> assertEquals(savedUser.getId(), testUser2.getId()),
                () -> assertEquals(savedUser.getUsername(), testUser2.getUsername()),
                () -> assertEquals(savedUser.getPassword(), testUser2.getPassword()),
                () -> assertEquals(userDao.findAll().size(), 2)
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
        Optional<User> foundUser = userDao.findByUsername(testUser.getUsername());
        Optional<User> notFoundPlayer = userDao.findByUsername(" ");

        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(testUser.getId(), foundUser.get().getId()),
                () -> assertEquals(testUser.getUsername(), foundUser.get().getUsername()),
                () -> assertEquals(testUser.getPassword(), foundUser.get().getPassword()),
                () -> assertFalse(notFoundPlayer.isPresent())
        );
    }
}
