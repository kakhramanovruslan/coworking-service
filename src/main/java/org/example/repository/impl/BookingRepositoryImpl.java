package org.example.dao.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.BookingDao;
import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BookingDao for managing Booking entities in the database.
 */
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {
    private final ConnectionManager connectionManager;
    /**
     * Retrieves all bookings from the database.
     *
     * @return List of all Booking objects retrieved from the database.
     */
    @Override
    public List<Booking> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.bookings;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                bookings.add(buildBooking(resultSet));
            }

            return bookings;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Finds a booking by its unique identifier.
     *
     * @param id The ID of the booking to find.
     * @return Optional containing the found Booking object, or empty if not found.
     */
    @Override
    public Optional<Booking> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM coworking.bookings
                WHERE id=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildBooking(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a booking by its unique identifier.
     *
     * @param id The ID of the booking to delete.
     * @return True if the booking was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteById(Long id){
        String sqlDeleteById = """
                DELETE FROM coworking.bookings
                WHERE id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes all from bookings table.
     *
     * @return True if the bookings was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteAll = """
            DELETE FROM coworking.bookings;
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteAll)) {
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return false;
        }
    }


    /**
     * Saves a new booking entity to the database.
     *
     * @param booking The Booking object to save.
     * @return The saved Booking object with its ID set, or null if saving failed.
     */
    @Override
    public Booking save(Booking booking) {
        if (isWorkspaceBooked(booking.getWorkspaceId(), booking.getStartTime(), booking.getEndTime())) {
            return null;
        }

        String sqlSave = """
            INSERT INTO coworking.bookings(workspace_id, user_id, start_time, end_time)
            VALUES (?,?,?,?);
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, booking.getWorkspaceId());
            preparedStatement.setObject(2, booking.getUserId());
            preparedStatement.setObject(3, booking.getStartTime());
            preparedStatement.setObject(4, booking.getEndTime());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                booking.setId(keys.getObject("id", Long.class));
            }

            return booking;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing booking entity in the database.
     *
     * @param booking The Booking object with updated values.
     * @return True if the update was successful, false otherwise.
     */
    @Override
    public boolean update(Booking booking) {
        return false;
    }

    /**
     * Finds all workspaces that are available within a specified time period.
     *
     * @param startTime The start time of the period.
     * @param endTime   The end time of the period.
     * @return List of Workspace objects that are available during the specified time period.
     */
    @Override
    public List<Workspace> findAllAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) {
        String sqlQuery = """
            SELECT w.id, w.name
            FROM coworking.workspaces w
            LEFT JOIN coworking.bookings b ON w.id = b.workspace_id
            AND (b.start_time < ? AND b.end_time > ?)
            WHERE b.workspace_id IS NULL;
            """;

        List<Workspace> workspaces = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startTime));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Workspace workspace = new Workspace();
                    workspace.setId(resultSet.getLong("id"));
                    workspace.setName(resultSet.getString("name"));

                    workspaces.add(workspace);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return workspaces;
    }

    /**
     * Retrieves all bookings that fall within a specified time period.
     *
     * @param startTime The start time of the period.
     * @param endTime   The end time of the period.
     * @return List of Booking objects within the specified time period.
     */
    @Override
    public List<Booking> getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        String sqlQuery = """
                SELECT id, workspace_id, user_id, start_time, end_time
                FROM coworking.bookings
                WHERE (start_time BETWEEN ? AND ?)
                OR (end_time BETWEEN ? AND ?)
                OR (start_time <= ? AND end_time >= ?);
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(endTime));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getLong("id"));
                booking.setWorkspaceId(resultSet.getLong("workspace_id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return bookings;
    }

    /**
     * Retrieves all bookings made by a specific user within a specified time period.
     *
     * @param username The username of the user to filter bookings by.
     * @return List of Booking objects made by the specified user.
     */
    @Override
    public List<Booking> getFilteredBookingsByUsername(String username) {
        String sqlQuery = """
                SELECT b.id, b.workspace_id, b.user_id, b.start_time, b.end_time
                FROM coworking.bookings b
                JOIN coworking.users u ON b.user_id = u.id
                WHERE u.username = ?;
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getLong("id"));
                booking.setWorkspaceId(resultSet.getLong("workspace_id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return bookings;
    }

    /**
     * Retrieves all bookings associated with a specific workspace.
     *
     * @param workspaceName The name of the workspace to filter bookings by.
     * @return List of Booking objects associated with the specified workspace.
     */
    @Override
    public List<Booking> getFilteredBookingsByWorkspace(String workspaceName) {
        String sqlQuery = """
                SELECT b.id, b.workspace_id, b.user_id, b.start_time, b.end_time
                FROM coworking.bookings b
                JOIN coworking.workspaces w ON b.workspace_id = w.id
                WHERE w.name = ?;
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, workspaceName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getLong("id"));
                booking.setWorkspaceId(resultSet.getLong("workspace_id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return bookings;
    }

    /**
     * Checks if a workspace is already booked during a specified time period.
     *
     * @param workspaceId The ID of the workspace to check.
     * @param startTime   The start time of the period to check.
     * @param endTime     The end time of the period to check.
     * @return True if the workspace is booked during the specified time period, false otherwise.
     */
    private boolean isWorkspaceBooked(Long workspaceId, LocalDateTime startTime, LocalDateTime endTime) {
        String sqlCheck = """
            SELECT COUNT(*) AS count
            FROM coworking.bookings
            WHERE workspace_id = ?
            AND ((start_time <= ? AND end_time > ?)
                OR (start_time < ? AND end_time >= ?)
                OR (start_time >= ? AND end_time <= ?));
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCheck)) {

            preparedStatement.setLong(1, workspaceId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(endTime));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
        }

        return false;
    }

    /**
     * Builds a Booking object from a ResultSet.
     *
     * @param resultSet The ResultSet containing booking data.
     * @return Booking object built from the ResultSet data.
     * @throws SQLException If an SQL exception occurs while retrieving data from the ResultSet.
     */
    private Booking buildBooking(ResultSet resultSet) throws SQLException {
        return Booking.builder()
                .id(resultSet.getLong("id"))
                .workspaceId(resultSet.getLong("workspace_id"))
                .userId(resultSet.getLong("user_id"))
                .startTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                .build();
    }

}
