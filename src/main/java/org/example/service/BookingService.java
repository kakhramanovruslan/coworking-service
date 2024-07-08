package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.dao.BookingDao;
import org.example.dto.BookingRequest;
import org.example.dto.UserDTO;
import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.entity.types.ActionType;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WorkspaceAlreadyBookedException;
import org.example.exceptions.WorkspaceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing bookings of workspaces.
 */
@RequiredArgsConstructor
public class BookingService {
    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final BookingDao bookingDao;

    /**
     * Retrieves a list of all available workspaces at the current time.
     * @return List of available workspaces
     */
    public List<Workspace> getAvailableWorkspacesAtNow() {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookingDao.findAllAvailableWorkspaces(currentTime, currentTime);
    }

    /**
     * Retrieves a list of available workspaces for a specified time period.
     * @param startTime Start time of the period
     * @param endTime End time of the period
     * @return List of available workspaces
     */
    public List<Workspace> getAvailableWorkspacesForTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findAllAvailableWorkspaces(startTime, endTime);
    }

    /**
     * Books a workspace for a user within a specified time period.
     * @param bookingRequest DTO with info about booking
     * @param username Username of the user booking the workspace
     * @return The booking entity that was created
     * @throws WorkspaceNotFoundException If the specified workspace does not exist
     * @throws UserNotFoundException If the user specified by username does not exist
     * @throws WorkspaceAlreadyBookedException If the workspace is already booked for the specified period
     */
    @Auditable(actionType = ActionType.BOOK_WORKSPACE)
    public Booking bookWorkspace(BookingRequest bookingRequest, String username) throws WorkspaceNotFoundException, UserNotFoundException, WorkspaceAlreadyBookedException {
        Optional<Workspace> workspace = workspaceService.getWorkspaceByName(bookingRequest.getWorkspaceName());
        if (workspace.isEmpty()) throw new WorkspaceNotFoundException("Workspace with this name was not found.");
        UserDTO user = userService.getUser(username);

        // checking overlapping bookings.
        boolean bookingExists = bookingDao.findAll().stream()
                .anyMatch(booking -> booking.getWorkspaceId().equals(workspace.get().getId()) &&
                        (booking.getEndTime().isAfter(bookingRequest.getStartTime()) ||
                                booking.getEndTime().isEqual(bookingRequest.getStartTime())) &&
                        (booking.getStartTime().isBefore(bookingRequest.getEndTime()) ||
                                booking.getStartTime().isEqual(bookingRequest.getEndTime())));

        if (bookingExists) {
            throw new WorkspaceAlreadyBookedException("The workspace is already booked for the specified period.");
        }

        Booking booking = bookingDao.save(Booking.builder()
                    .workspaceId(workspace.get().getId())
                    .userId(user.getId())
                    .startTime(bookingRequest.getStartTime())
                    .endTime(bookingRequest.getEndTime())
                    .build());
        return booking;
    }

    /**
     * Cancels a booking by its ID.
     * @param id ID of the booking to cancel
     * @return True if the booking was successfully canceled, false otherwise
     */
    public boolean cancelBook(Long id) {
        return bookingDao.deleteById(id);
    }

    /**
     * Retrieves a list of bookings filtered by a specified time period.
     * @param startTime Start time of the period
     * @param endTime End time of the period
     * @return List of bookings within the specified time period
     */
    public List<Booking> getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime);
    }

    /**
     * Retrieves a list of bookings filtered by a specific username.
     * @param username Username of the user whose bookings are to be retrieved
     * @return List of bookings made by the specified user
     * @throws UserNotFoundException If the specified user does not exist
     */
    public List<Booking> getFilteredBookingsByUsername(String username) throws UserNotFoundException {
        userService.getUser(username);
        return bookingDao.getFilteredBookingsByUsername(username);
    }

    /**
     * Retrieves a list of bookings filtered by a specific workspace name.
     * @param workspaceName Name of the workspace whose bookings are to be retrieved
     * @return List of bookings for the specified workspace
     * @throws WorkspaceNotFoundException If the specified workspace does not exist
     */
    public List<Booking> getFilteredBookingsByWorkspace(String workspaceName) throws WorkspaceNotFoundException {
        workspaceService.getWorkspace(workspaceName);
        return bookingDao.getFilteredBookingsByWorkspace(workspaceName);
    }

}
