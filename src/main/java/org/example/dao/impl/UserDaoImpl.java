package org.example.dao.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.types.Role;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserDao interface for interacting with User entities in the database.
 */
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final ConnectionManager connectionManager;

    /**
     * Retrieves a user by their ID.
     * @param id ID of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    @Override
    public Optional<User> findById(Long id){
        String sqlFindById = """
                SELECT * FROM coworking.users
                WHERE id=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildUser(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a user by their ID.
     * @param id ID of the user to delete
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteById(Long id) {
        String sqlDeleteById = """
                DELETE FROM coworking.users
                WHERE id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes all from user table.
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteAll = """
            DELETE FROM coworking.users;
            """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteAll)) {
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }


    /**
     * Retrieves all users from the database.
     * @return List of all users
     */
    @Override
    public List<User> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.users;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }

            return users;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves a new user to the database.
     * @param user User object to save
     * @return The saved user object
     */
    @Override
    public User save(User user){
        String sqlSave = """
                INSERT INTO coworking.users(username, password, role)
                VALUES (?,?,?);
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                user.setId(keys.getObject("id", Long.class));
            }

            return user;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates existing user.
     * @param user User object to update
     * @return Always returns false as update operation is not implemented
     */
    @Override
    public boolean update(User user) {
        return false;
    }

    /**
     * Retrieves a user by their username.
     * @param username Username of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    @Override
    public Optional<User> findByUsername(String username){
        String sqlFindByUsername = """
                SELECT * FROM coworking.users
                WHERE username=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByUsername)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(buildUser(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Builds a User object from the ResultSet.
     * @param resultSet ResultSet containing user data
     * @return User object built from the ResultSet
     * @throws SQLException If an SQL exception occurs
     */
    private User buildUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .role(Role.valueOf(resultSet.getString("role")))
                .build();
    }

}
