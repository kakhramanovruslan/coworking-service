package org.example.dao.impl;

import org.example.dao.Dao;
import org.example.entity.User;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements Dao<Long, User> {

    private static final UserDaoImpl userDaoImpl = new UserDaoImpl();

    @Override
    public Optional<User> findById(Long id){
        String sqlFindById = """
                SELECT * FROM coworking.users
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public boolean deleteById(Long id) {
        String sqlDeleteById = """
                DELETE FROM coworking.users
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
    public List<User> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.users;
                """;

        try (Connection connection = ConnectionManager.getConnection();
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

    @Override
    public User save(User user){
        String sqlSave = """
                INSERT INTO coworking.users(username, password)
                VALUES (?,?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

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

    @Override
    public boolean update(User user) {
        return false;
    }

    public Optional<User> findByUsername(String username){
        String sqlFindByUsername = """
                SELECT * FROM coworking.users
                WHERE username=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByUsername)) {
            preparedStatement.setObject(1, username);
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

    private User buildUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .build();
    }


    public static UserDaoImpl getInstance() {
        return userDaoImpl;
    }
}
