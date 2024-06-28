package org.example.service;

import org.example.dao.impl.BookingDaoImpl;
import org.example.entity.Booking;
import org.example.entity.User;
import org.example.entity.Workspace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private static BookingService bookingService = new BookingService();

    private final WorkspaceService workspaceService = WorkspaceService.getInstance();
    private final UserService userService = UserService.getInstance();

    private final BookingDaoImpl bookingDao = BookingDaoImpl.getInstance();

    public List<Workspace> getAvailableWorkspacesAtNow() {
        LocalDateTime currentTime = LocalDateTime.now();
        return bookingDao.findAllAvailableWorkspaces(currentTime, currentTime);
    }

    public List<Workspace> getAvailableWorkspacesForTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findAllAvailableWorkspaces(startTime, endTime);
    }

    public Booking bookWorkspace(String workspaceName, String username, LocalDateTime startTime, LocalDateTime endTime) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceName);
        Optional<User> user = userService.getUser(username);
        Booking booking = bookingDao.save(Booking.builder()
                        .workspaceId(workspace.get().getId())
                        .userId(user.get().getId())
                        .startTime(startTime)
                        .endTime(endTime)
                        .build());
        return booking;
    }

    public boolean cancelBook(Long id) {
        return bookingDao.deleteById(id);
    }

    public List<Booking> getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.getFilteredBookingsByTimePeriod(startTime, endTime);
    }

    public List<Booking> getFilteredBookingsByUsername(String username) {
        return bookingDao.getFilteredBookingsByUsername(username);
    }

    public List<Booking> getFilteredBookingsByWorkspace(String workspaceName) {
        return bookingDao.getFilteredBookingsByWorkspace(workspaceName);
    }

        public static BookingService getInstance() {
        return bookingService;
    }
}
