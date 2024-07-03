package org.example.dao;

import org.example.entity.Booking;
import org.example.entity.Workspace;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for Booking Data Access Object (DAO).
 * Extends the base Dao interface for generic CRUD operations.
 * Additional method to find all available workspaces within a specified time range.
 */
public interface BookingDao extends Dao<Long, Booking> {

    /**
     * Finds all available workspaces within a specified time range.
     *
     * @param startTime the start time of the range to check for availability
     * @param endTime the end time of the range to check for availability
     * @return a list of workspaces that are available within the specified time range
     */
    List<Workspace> findAllAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Retrieves all bookings that fall within a specified time period.
     *
     * @param startTime The start time of the period.
     * @param endTime   The end time of the period.
     * @return List of Booking objects within the specified time period.
     */
    List<Booking> getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Retrieves all bookings made by a specific user within a specified time period.
     *
     * @param username The username of the user to filter bookings by.
     * @return List of Booking objects made by the specified user.
     */
    List<Booking> getFilteredBookingsByUsername(String username);

    /**
     * Retrieves all bookings associated with a specific workspace.
     *
     * @param workspaceName The name of the workspace to filter bookings by.
     * @return List of Booking objects associated with the specified workspace.
     */
    List<Booking> getFilteredBookingsByWorkspace(String workspaceName);
}
