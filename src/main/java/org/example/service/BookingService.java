package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.impl.BookingDaoImpl;
import org.example.entity.Booking;
import org.example.entity.User;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing bookings of workspaces.
 */
public class BookingService {
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final BookingDaoImpl bookingDao;

    public BookingService(WorkspaceService workspaceService, UserService userService, ConnectionManager connectionManager) {
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.bookingDao = new BookingDaoImpl(connectionManager);
    }

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
     * @param workspaceName Name of the workspace
     * @param username Username of the user booking the workspace
     * @param startTime Start time of the booking
     * @param endTime End time of the booking
     * @return The booking entity that was created
     */
    public Booking bookWorkspace(String workspaceName, String username, LocalDateTime startTime, LocalDateTime endTime) throws NoSuchElementException{
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceName);
        if (workspace.isEmpty()) throw new NoSuchElementException();
        Optional<User> user = userService.getUser(username);
        Booking booking = bookingDao.save(Booking.builder()
                    .workspaceId(workspace.get().getId())
                    .userId(user.get().getId())
                    .startTime(startTime)
                    .endTime(endTime)
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
     */
    public List<Booking> getFilteredBookingsByUsername(String username) {
        return bookingDao.getFilteredBookingsByUsername(username);
    }

    /**
     * Retrieves a list of bookings filtered by a specific workspace name.
     * @param workspaceName Name of the workspace whose bookings are to be retrieved
     * @return List of bookings for the specified workspace
     */
    public List<Booking> getFilteredBookingsByWorkspace(String workspaceName) {
        return bookingDao.getFilteredBookingsByWorkspace(workspaceName);
    }

}
