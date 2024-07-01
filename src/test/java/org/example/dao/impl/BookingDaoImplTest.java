package org.example.dao.impl;

import org.example.config.ContainersEnvironment;
import org.example.entity.Booking;
import org.example.entity.User;
import org.example.entity.Workspace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDaoImplTest extends ContainersEnvironment {

    BookingDaoImpl bookingDao;
    UserDaoImpl userDao;
    WorkspaceDaoImpl workspaceDao;
    private Booking testBooking;
    private User testUser;
    private Workspace testWorkspace;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @BeforeEach
    public void setUp(){
        bookingDao = BookingDaoImpl.getInstance();
        userDao = UserDaoImpl.getInstance();
        workspaceDao = WorkspaceDaoImpl.getInstance();

        bookingDao.deleteAll();
        userDao.deleteAll();
        workspaceDao.deleteAll();

        testUser = testUser.builder()
                .username("ruslan")
                .password("123")
                .build();
        userDao.save(testUser);

        testWorkspace = testWorkspace.builder()
                .name("test-workspace-1")
                .build();
        workspaceDao.save(testWorkspace);

        LocalDateTime startTime = LocalDateTime.parse("2024-06-21 11:30", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2024-06-21 12:30", formatter);

        testBooking = testBooking.builder()
                        .workspaceId(testWorkspace.getId())
                        .userId(testUser.getId())
                        .startTime(startTime)
                        .endTime(endTime)
                        .build();
        bookingDao.save(testBooking);
    }

    @AfterEach
    public void reset(){
        userDao.deleteAll();
        workspaceDao.deleteAll();
        bookingDao.deleteAll();
    }

    @Test
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
    public void testDeleteById(){
        assertAll(
                () -> assertThat(bookingDao.deleteById(testBooking.getId())).isTrue(),
                () -> assertThat(bookingDao.findById(testBooking.getId()).isEmpty()).isTrue()
        );
    }

    @Test
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
    @Disabled
    public void testUpdate(){
    }

    @Test
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
    public void testGetFilteredBookingsByUsername(){
        assertAll(
                () -> assertThat(bookingDao.getFilteredBookingsByUsername(testUser.getUsername())).hasSize(1),
                () -> assertThat(bookingDao.getFilteredBookingsByUsername("")).hasSize(0)
        );
    }

    @Test
    public void testGetFilteredBookingsByWorkspace(){
        assertAll(
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace(testWorkspace.getName())).hasSize(1),
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace("")).hasSize(0),
                () -> assertThat(bookingDao.getFilteredBookingsByWorkspace(testWorkspace.getName()).get(0).getWorkspaceId()).isEqualTo(testWorkspace.getId())
        );
    }

}
