package org.example.dao.impl;

import org.example.dao.Dao;
import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDaoImpl implements Dao<Long, Booking> {
    private static final BookingDaoImpl bookingDaoImpl = new BookingDaoImpl();
    @Override
    public List<Booking> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.bookings;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                bookings.add(buildBooking(resultSet));
            }

            return bookings;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM coworking.bookings
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildBooking(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(Long id){
        String sqlDeleteById = """
                DELETE FROM coworking.bookings
                WHERE id = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Booking save(Booking booking){
        String sqlSave = """
                INSERT INTO coworking.bookings(workspace_id, user_id, start_time, end_time)
                VALUES (?,?,?,?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, booking.getWorkspaceId());
            preparedStatement.setObject(2, booking.getUserId());
            preparedStatement.setObject(2, booking.getStartTime());
            preparedStatement.setObject(2, booking.getEndTime());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                booking.setId(keys.getObject("id", Long.class));
            }

            return booking;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Booking booking) {
        return false;
    }

    public List<Workspace> findAllAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) {
        String sqlQuery = """
            SELECT w.id, w.name
            FROM coworking.workspaces w
            LEFT JOIN coworking.bookings b ON w.id = b.workspace_id
            AND (b.startTime < ? AND b.endTime > ?)
            WHERE b.workspace_id IS NULL;
            """;

        List<Workspace> workspaces = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
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
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return workspaces;
    }

    public List<Booking> getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        String sqlQuery = """
                SELECT id, workspace_id, user_id, start_time, end_time
                FROM coworking.bookings
                WHERE (start_time BETWEEN ? AND ?)
                OR (end_time BETWEEN ? AND ?)
                OR (start_time <= ? AND end_time >= ?);
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
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
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return bookings;
    }

    public List<Booking> getFilteredBookingsByUsername(String username) {
        String sqlQuery = """
                SELECT b.workspace_id, b.user_id, b.start_time, b.end_time
                FROM coworking.bookings b
                JOIN coworking.users u ON b.user_id = u.id
                WHERE u.username = ?;
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setWorkspaceId(resultSet.getLong("workspace_id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return bookings;
    }

    public List<Booking> getFilteredBookingsByWorkspace(String workspaceName) {
        String sqlQuery = """
                SELECT b.workspace_id, b.user_id, b.start_time, b.end_time
                FROM coworking.bookings b
                JOIN coworking.workspaces w ON b.workspace_id = w.id
                WHERE w.name = ?;
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, workspaceName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setWorkspaceId(resultSet.getLong("workspace_id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
                booking.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return bookings;
    }


    private Booking buildBooking(ResultSet resultSet) throws SQLException {
        return Booking.builder()
                .id(resultSet.getLong("id"))
                .workspaceId(resultSet.getLong("workspace_id"))
                .userId(resultSet.getLong("user_id"))
                .startTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                .endTime(resultSet.getTimestamp("end_time").toLocalDateTime())
                .build();
    }


    public static BookingDaoImpl getInstance() {
        return bookingDaoImpl;
    }
}
