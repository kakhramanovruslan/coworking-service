package org.example.dao.impl;

import org.example.entity.User;
import org.example.liquibase.LiquibaseDemo;
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

    private static UserDaoImpl userDao;
    private User testUser;

    @BeforeAll
    public static void setUpAll() {
        postgresContainer.start();

        ConnectionManager connectionManager = new ConnectionManager(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations(connectionManager.getConnection());

        userDao = new UserDaoImpl(connectionManager);
    }

    @BeforeEach
    public void setUp(){

        userDao.deleteAll();

        testUser = testUser.builder()
                .username("ruslan")
                .password("123")
                .build();
        userDao.save(testUser);
    }

    @AfterEach
    public void reset(){
        userDao.deleteAll();
    }

    @AfterAll
    public static void resetAll(){
        postgresContainer.stop();
    }

    @Test
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
    public void testDeleteById(){
        assertTrue(userDao.deleteById(testUser.getId()));
        assertTrue(userDao.findById(testUser.getId()).isEmpty());
    }

    @Test
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
    @Disabled
    public void testUpdate(){
    }

    @Test
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
