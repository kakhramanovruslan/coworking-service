package org.example.service;

import org.example.dao.BookingDao;
import org.example.dao.Dao;
import org.example.dao.impl.BookingDaoImpl;
import org.example.entity.Booking;
import org.example.entity.User;
import org.example.entity.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private WorkspaceService workspaceService;
    @Mock
    private UserService userService;
    @Mock
    private BookingDao bookingDao;
    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("Test retrieving available workspaces at current time")
    void testGetAvailableWorkspacesAtNow() {
        Workspace workspace1 = Workspace.builder().name("Workspace 1").build();
        Workspace workspace2 = Workspace.builder().name("Workspace 2").build();
        List<Workspace> availableWorkspaces = Arrays.asList(workspace1, workspace2);
        when(bookingDao.findAllAvailableWorkspaces(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(availableWorkspaces);

        List<Workspace> result = bookingService.getAvailableWorkspacesAtNow();

        assertEquals(
                availableWorkspaces.stream().map(Workspace::getName).toList(),
                result.stream().map(Workspace::getName).toList()
        );

        verify(bookingDao).findAllAvailableWorkspaces(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test retrieving available workspaces for a specific time period")
    void testGetAvailableWorkspacesForTimePeriod() {
        Workspace workspace1 = Workspace.builder().name("Workspace 1").build();
        Workspace workspace2 = Workspace.builder().name("Workspace 2").build();
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        List<Workspace> availableWorkspaces = Arrays.asList(workspace1, workspace2);
        when(bookingDao.findAllAvailableWorkspaces(startTime, endTime)).thenReturn(availableWorkspaces);

        List<Workspace> result = bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime);

        assertEquals(availableWorkspaces, result);
        verify(bookingDao).findAllAvailableWorkspaces(startTime, endTime);
    }

    @Test
    @DisplayName("Test canceling when booking does not exist")
    void testCancelBookWhenBookingDoesNotExist() {
        Long nonExistingBookingId = 999L;
        when(bookingDao.deleteById(nonExistingBookingId)).thenReturn(false);

        boolean result = bookingService.cancelBook(nonExistingBookingId);

        assertFalse(result);
        verify(bookingDao).deleteById(nonExistingBookingId);
    }

    @Test
    @DisplayName("Test booking a workspace")
    void testBookWorkspace() {
        String workspaceName = "Workspace 1";
        String username = "User 1";
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Workspace workspace = Workspace.builder().name(workspaceName).build();
        User user = User.builder().username(username).build();
        Booking booking = Booking.builder()
                                 .workspaceId(workspace.getId())
                                 .userId(user.getId())
                                 .startTime(startTime)
                                 .endTime(endTime)
                                 .build();
        when(workspaceService.getWorkspace(workspaceName)).thenReturn(Optional.of(workspace));
        when(userService.getUser(username)).thenReturn(Optional.of(user));
        when(bookingDao.save(booking)).thenReturn(booking);

        Booking result = bookingService.bookWorkspace(workspaceName, username, startTime, endTime);

        assertEquals(booking, result);
        verify(workspaceService).getWorkspace(workspaceName);
        verify(userService).getUser(username);
        verify(bookingDao).save(booking);
    }

    @Test
    @DisplayName("Test canceling a booking")
    void testCancelBook() {
        Long bookingId = 1L;
        when(bookingDao.deleteById(bookingId)).thenReturn(true);

        boolean result = bookingService.cancelBook(bookingId);

        assertTrue(result);
        verify(bookingDao).deleteById(bookingId);
    }

    @Test
    @DisplayName("Test retrieving filtered bookings by time period")
    void testGetFilteredBookingsByTimePeriod() {
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Booking booking1 = Booking.builder()
                .workspaceId(1L)
                .userId(1L)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Booking booking2 = Booking.builder()
                .workspaceId(2L)
                .userId(1L)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime)).thenReturn(bookings);

        List<Booking> result = bookingService.getFilteredBookingsByTimePeriod(startTime, endTime);

        assertEquals(bookings, result);
        verify(bookingDao).getFilteredBookingsByTimePeriod(startTime, endTime);
    }

    @Test
    @DisplayName("Test retrieving filtered bookings by username")
    void testGetFilteredBookingsByUsername() {
        String username = "User 1";
        Booking booking1 = Booking.builder()
                .workspaceId(1L)
                .userId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        Booking booking2 = Booking.builder()
                .workspaceId(2L)
                .userId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingDao.getFilteredBookingsByUsername(username)).thenReturn(bookings);

        List<Booking> result = bookingService.getFilteredBookingsByUsername(username);

        assertEquals(bookings, result);
        verify(bookingDao).getFilteredBookingsByUsername(username);
    }

    @Test
    @DisplayName("Test retrieving filtered bookings by workspace")
    void testGetFilteredBookingsByWorkspace() {
        String workspaceName = "Workspace 1";
        Booking booking1 = Booking.builder()
                .workspaceId(1L)
                .userId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        Booking booking2 = Booking.builder()
                .workspaceId(2L)
                .userId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingDao.getFilteredBookingsByWorkspace(workspaceName)).thenReturn(bookings);

        List<Booking> result = bookingService.getFilteredBookingsByWorkspace(workspaceName);

        assertEquals(bookings, result);
        verify(bookingDao).getFilteredBookingsByWorkspace(workspaceName);
    }

    @Test
    @DisplayName("Test bookWorkspace throws NoSuchElementException when workspace not found")
    void testBookWorkspaceNoSuchElementException() {
        String workspaceName = "Workspace 1";
        String username = "User 1";
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        when(workspaceService.getWorkspace(workspaceName)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.bookWorkspace(workspaceName, username, startTime, endTime));
        verify(workspaceService).getWorkspace(workspaceName);
    }

    @Test
    @DisplayName("Test bookWorkspace throws NoSuchElementException when username not found")
    void testBookWorkspaceUsernameNotFoundException() {
        String workspaceName = "Workspace 1";
        String username = "User 1";
        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        Workspace workspace = Workspace.builder().name(workspaceName).build();
        when(workspaceService.getWorkspace(workspaceName)).thenReturn(Optional.of(workspace));
        when(userService.getUser(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.bookWorkspace(workspaceName, username, startTime, endTime));
        verify(workspaceService).getWorkspace(workspaceName);
        verify(userService).getUser(username);
    }
}