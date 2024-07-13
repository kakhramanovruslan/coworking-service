package org.example.service;

import org.example.repository.BookingRepository;
import org.example.dto.BookingRequest;
import org.example.dto.UserDTO;
import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WorkspaceAlreadyBookedException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private BookingRepository bookingDao;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("Test booking a workspace successfully")
    void testBookWorkspaceSuccess() throws WorkspaceNotFoundException, UserNotFoundException, WorkspaceAlreadyBookedException {
        String username = "testUser";
        String workspaceName = "Test Workspace";
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(3);

        BookingRequest bookingRequest = buildBookingRequest(workspaceName, startTime, endTime);
        Workspace workspace = buildWorkspace(1L, workspaceName);
        UserDTO userDTO = buildUserDTO(1L, username);

        when(workspaceService.getWorkspace(workspaceName)).thenReturn(workspace);
        when(userService.getUser(username)).thenReturn(userDTO);
        when(bookingDao.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L);
            return savedBooking;
        });

        Booking booking = bookingService.bookWorkspace(bookingRequest, username);

        assertNotNull(booking);
        assertEquals(workspace.getId(), booking.getWorkspaceId());
        assertEquals(userDTO.getId(), booking.getUserId());
        assertEquals(startTime, booking.getStartTime());
        assertEquals(endTime, booking.getEndTime());
        verify(workspaceService, times(1)).getWorkspace(workspaceName);
        verify(userService, times(1)).getUser(username);
        verify(bookingDao, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Test cancel a booking successfully")
    void testCancelBookingSuccess() {
        Long bookingId = 1L;

        when(bookingDao.deleteById(bookingId)).thenReturn(true);

        boolean result = bookingService.cancelBook(bookingId);

        assertTrue(result);
        verify(bookingDao, times(1)).deleteById(bookingId);
    }

    @Test
    @DisplayName("Test retrieving filtered bookings by time period")
    void testGetFilteredBookingsByTimePeriod() {
        String startTimeStr = "2024-07-01T10:00:00";
        String endTimeStr = "2024-07-01T12:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        Booking booking1 = buildBooking(1L, 1L, startTime, endTime);
        Booking booking2 = buildBooking(2L, 2L, startTime.plusDays(1), endTime.plusDays(1));
        List<Booking> expectedBookings = Arrays.asList(booking1, booking2);

        when(bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bookingService.getFilteredBookingsByTimePeriod(startTimeStr, endTimeStr);

        assertNotNull(actualBookings);
        assertEquals(expectedBookings.size(), actualBookings.size());
        assertEquals(expectedBookings.get(0), actualBookings.get(0));
        assertEquals(expectedBookings.get(1), actualBookings.get(1));
        verify(bookingDao, times(1)).getFilteredBookingsByTimePeriod(startTime, endTime);
    }


    @Test
    @DisplayName("Test retrieving filtered bookings by username")
    void testGetFilteredBookingsByUsername() throws UserNotFoundException {
        String username = "testUser";
        UserDTO userDTO = buildUserDTO(1L, username);

        Booking mockBook1 = buildBooking(userDTO.getId(), 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking mockBook2 = buildBooking(userDTO.getId(), 2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        List<Booking> mockBookings = Arrays.asList(mockBook1, mockBook2);

        when(userService.getUser(username)).thenReturn(userDTO);
        when(bookingDao.getFilteredBookingsByUsername(username)).thenReturn(mockBookings);

        List<Booking> bookings = bookingService.getFilteredBookingsByUsername(username);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        verify(userService, times(1)).getUser(username);
        verify(bookingDao, times(1)).getFilteredBookingsByUsername(username);
    }

    @Test
    @DisplayName("Test retrieving filtered bookings by workspace name")
    void testGetFilteredBookingsByWorkspace() throws WorkspaceNotFoundException {
        String workspaceName = "Test Workspace";
        Workspace workspace = buildWorkspace(1L, workspaceName);

        Booking mockBook1 = buildBooking(1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking mockBook2 = buildBooking(2L, 2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));
        List<Booking> mockBookings = Arrays.asList(mockBook1, mockBook2);

        when(workspaceService.getWorkspace(workspaceName)).thenReturn(workspace);
        when(bookingDao.getFilteredBookingsByWorkspace(workspaceName)).thenReturn(mockBookings);

        List<Booking> bookings = bookingService.getFilteredBookingsByWorkspace(workspaceName);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        verify(workspaceService, times(1)).getWorkspace(workspaceName);
        verify(bookingDao, times(1)).getFilteredBookingsByWorkspace(workspaceName);
    }

    public UserDTO buildUserDTO(Long id, String username){
        return UserDTO.builder()
                .id(id)
                .username(username)
                .build();
    }

    public Workspace buildWorkspace(Long id, String name){
        return Workspace.builder()
                .id(id)
                .name(name)
                .build();
    }

    public BookingRequest buildBookingRequest(String workspaceName, LocalDateTime startTime, LocalDateTime endTime){
        return BookingRequest.builder()
                .workspaceName(workspaceName)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public Booking buildBooking(Long userId, Long workspaceId, LocalDateTime startTime, LocalDateTime endTime){
        return Booking.builder()
                .userId(userId)
                .workspaceId(workspaceId)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

}
