package org.example.service;

import org.example.dao.impl.BookingDaoImpl;
import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.entity.Booking;
import org.example.entity.Workspace;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookingService {
    private static BookingService bookingService = new BookingService();

    private final BookingDaoImpl bookingDao = BookingDaoImpl.getInstance();

    public List<Workspace> getAvailableWorkspacesAtNow() {
        return bookingDao.findAllAvailableWorkspaces(LocalDateTime.now(), LocalDateTime.now());
    }

    public List<Workspace> getAvailableWorkspacesForTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingDao.findAllAvailableWorkspaces(startTime, endTime);
    }

    public Booking bookWorkspace(Long workspaceId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Booking booking = bookingDao.save(Booking.builder()
                        .workspaceId(workspaceId)
                        .userId(userId)
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
