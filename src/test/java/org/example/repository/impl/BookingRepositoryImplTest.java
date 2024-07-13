package org.example.dao.impl;

import org.example.dao.BookingDao;
import org.example.dao.UserDao;
import org.example.dao.WorkspaceDao;
import org.example.entity.Booking;
import org.example.entity.User;
import org.example.entity.Workspace;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class BookingDaoImplTest {

    private static BookingDao bookingDao;
    private static UserDao userDao;
    private static WorkspaceDao workspaceDao;
    private ConnectionManager connectionManager;
    private LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
    private Booking testBooking;
    private User testUser;
    private Workspace testWorkspace;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
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

        userDao = new UserDaoImpl(connectionManager);
        workspaceDao = new WorkspaceDaoImpl(connectionManager);
        bookingDao = new BookingDaoImpl(connectionManager);

        userDao.deleteById(-1L); // delete admin

        testUser = userDao.save(User.builder()
                                    .username("ruslan")
                                    .password("123")
                                    .build());

        testWorkspace = workspaceDao.save(Workspace.builder()
                                                   .name("test-workspace-1")
                                                   .build());

        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 11:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 12:30", formatter);

        testBooking = bookingDao.save(Booking.builder()
                                             .workspaceId(testWorkspace.getId())
                                             .userId(testUser.getId())
                                             .startTime(startTime)
                                             .endTime(endTime)
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
    @DisplayName("Test findAll method")
    public void testFindAll(){
        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 13:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 14:30", formatter);
        Booking testBooking2 = Booking.builder()
                .workspaceId(testWorkspace.getId())
                .userId(testUser.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        bookingDao.save(testBooking2);

        assertAll(
                () -> assertThat(bookingDao.findAll()).hasSize(2),
                () -> assertThat(bookingDao.findAll().isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("Test findById method")
    public void testFindById(){
        Optional<Booking> foundBooking = bookingDao.findById(testBooking.getId());
        Optional<Booking> notFoundBooking = bookingDao.findById(999L);
        assertAll(
                () -> assertTrue(foundBooking.isPresent()),
                () -> assertThat(testBooking.getId()).isEqualTo(foundBooking.get().getId()),
                () -> assertThat(testBooking.getWorkspaceId()).isEqualTo(foundBooking.get().getWorkspaceId()),
                () -> assertFalse(notFoundBooking.isPresent())
        );
    }

    @Test
    @DisplayName("Test deleteById method")
    public void testDeleteById(){
        assertAll(
                () -> assertThat(bookingDao.deleteById(testBooking.getId())).isTrue(),
                () -> assertThat(bookingDao.findById(testBooking.getId()).isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("Test deleteAll method")
    public void testDeleteAll(){
        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 13:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 14:30", formatter);
        Booking testBooking2 = Booking.builder()
                .workspaceId(testWorkspace.getId())
                .userId(testUser.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        bookingDao.save(testBooking2);

        assertAll(
                () -> assertThat(bookingDao.deleteAll()).isTrue(),
                () -> assertThat(bookingDao.findAll().size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("Test save method returns null if workspace is booked")
    public void testSaveReturnNullIfWorkspaceBooked(){
        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 11:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 12:30", formatter);
        Booking testBooking2 = Booking.builder()
                .workspaceId(testWorkspace.getId())
                .userId(testUser.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Booking savedBooking = bookingDao.save(testBooking2);

        assertAll(
                () -> assertThat(savedBooking).isNull()
        );
    }

    @Test
    @DisplayName("Test save method")
    public void testSave(){
        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 13:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 14:30", formatter);
        Booking testBooking2 = Booking.builder()
                .workspaceId(testWorkspace.getId())
                .userId(testUser.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Booking savedBooking = bookingDao.save(testBooking2);

        assertAll(
                () -> assertThat(bookingDao.findById(savedBooking.getId()).isPresent()).isTrue(),
                () -> assertThat(savedBooking.getId()).isEqualTo(testBooking2.getId()),
                () -> assertThat(savedBooking.getWorkspaceId()).isEqualTo(testBooking2.getWorkspaceId()),
                () -> assertThat(bookingDao.findAll().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("Test update method")
    @Disabled("Not implemented yet")
    public void testUpdate(){
    }

    @Test
    @DisplayName("Test findAllAvailableWorkspaces method")
    public void testFindAllAvailableWorkspaces() {
        Workspace testWorkspace2 = Workspace.builder()
                .name("test-workspace-2")
                .build();
        workspaceDao.save(testWorkspace2);

        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 11:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 12:30", formatter);

        assertAll(
                () -> assertThat(bookingDao.findAllAvailableWorkspaces(startTime, endTime).isEmpty()).isFalse(),
                () -> assertThat(bookingDao.findAllAvailableWorkspaces(startTime, endTime)).hasSize(1),
                () -> assertThat(bookingDao.findAllAvailableWorkspaces(startTime.plusHours(2), endTime.plusHours(2))).hasSize(2),
                () -> assertThat(bookingDao.findAllAvailableWorkspaces(startTime, endTime).get(0).getName()).isEqualTo(testWorkspace2.getName())
        );
    }

    @Test
    @DisplayName("Test testGetFilteredBookingsByTimePeriod method")
    public void testGetFilteredBookingsByTimePeriod(){
        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 11:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 12:30", formatter);
        assertAll(
                () -> assertThat(bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime).isEmpty()).isFalse(),
                () -> assertThat(bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime)).hasSize(1),
                () -> assertThat(bookingDao.getFilteredBookingsByTimePeriod(startTime.plusHours(2), endTime.plusHours(2))).isEmpty()
        );
    }

    @Test
    @DisplayName("Test getFilteredBookingsByUsername method")
    public void testGetFilteredBookingsByUsername(){
        assertAll(
                () -> assertThat(bookingDao.getFilteredBookingsByUsername(testUser.getUsername())).hasSize(1),
                () -> assertThat(bookingDao.getFilteredBookingsByUsername("")).hasSize(0)
        );
    }

    @Test
    @DisplayName("Test getFilteredBookingsByWorkspace method")
    public void testGetFilteredBookingsByWorkspace(){
        assertAll(
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace(testWorkspace.getName())).hasSize(1),
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace("")).hasSize(0),
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace(testWorkspace.getName()).get(0).getWorkspaceId()).isEqualTo(testWorkspace.getId())
        );
    }

}
