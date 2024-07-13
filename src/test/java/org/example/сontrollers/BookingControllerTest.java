package org.example.сontrollers;

import org.example.controllers.BookingController;
import org.example.dto.Authentication;
import org.example.dto.BookingRequest;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Booking;
import org.example.entity.types.Role;
import org.example.exceptions.WorkspaceAlreadyBookedException;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.ServletContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    @DisplayName("Test booking Workspace success")
    public void testBookWorkspace() throws Exception {
        BookingRequest request = buildBookingRequest("Workspace1", "2024-07-15T10:00:00", "2024-07-15T12:00:00");
        Booking booking = buildBooking(1L, 1L, "2024-07-15T10:00:00", "2024-07-15T12:00:00");

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("user1", Role.USER));
        when(bookingService.bookWorkspace(request, "user1")).thenReturn(booking);

        mockMvc.perform(post("/workspaces/bookings/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"workspaceName\": \"Workspace1\", \"startTime\": \"2024-07-15T10:00:00\", \"endTime\": \"2024-07-15T12:00:00\" }"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test booking Workspace Fail")
    public void testBookWorkspaceFail() {
        BookingRequest request = buildBookingRequest("Workspace1", "2024-07-15T10:00:00", "2024-07-15T12:00:00");

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("user1", Role.USER));
        when(bookingService.bookWorkspace(request, "user1")).thenThrow(new WorkspaceAlreadyBookedException("Workspace already booked."));

        WorkspaceAlreadyBookedException exception = assertThrows(WorkspaceAlreadyBookedException.class, () -> {
            bookingController.bookWorkspace(request);
        });

        assertEquals("Workspace already booked.", exception.getMessage());
    }

    @Test
    @DisplayName("Test Cancel Booking")
    public void testCancelBooking() throws Exception {
        String bookingId = "1";

        mockMvc.perform(delete("/workspaces/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).cancelBook(Long.parseLong(bookingId));
    }

    @Test
    @DisplayName("Test Get bookings by time period")
    public void testGetFilteredBookingsByTimePeriod() throws Exception {
        String startTime = "2024-07-15T00:00:00";
        String endTime = "2024-07-15T23:59:59";

        mockMvc.perform(get("/workspaces/bookings/time-period")
                        .param("startTime", startTime)
                        .param("endTime", endTime)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getFilteredBookingsByTimePeriod(startTime, endTime);
    }

    @Test
    @DisplayName("Test Get bookings by Username")
    public void testGetFilteredBookingsByUsername() throws Exception {
        String username = "user1";

        mockMvc.perform(get("/workspaces/bookings/username")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getFilteredBookingsByUsername(username);
    }

    @Test
    @DisplayName("Test Get bookings by Workspace Name")
    public void testGetFilteredBookingsByWorkspace() throws Exception {
        String workspaceName = "Workspace1";

        mockMvc.perform(get("/workspaces/bookings/workspace")
                        .param("name", workspaceName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getFilteredBookingsByWorkspace(workspaceName);
    }



    public BookingRequest buildBookingRequest(String workspaceName, String startTime, String endTime){
        return BookingRequest.builder()
                .workspaceName(workspaceName)
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .build();
    }

    public Booking buildBooking(Long userId, Long workspaceId, String startTime, String endTime){
        return Booking.builder()
                .userId(userId)
                .workspaceId(workspaceId)
                .startTime(LocalDateTime.parse(startTime))
                .endTime(LocalDateTime.parse(endTime))
                .build();
    }
}
