package org.example.dao.impl;

import org.example.dao.Dao;
import org.example.entity.User;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;

import java.lang.annotation.Native;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of Dao interface for interacting with Workspace entities in the database.
 */
public class WorkspaceDaoImpl implements Dao<Long, Workspace> {
    private static final WorkspaceDaoImpl workspaceDaoImpl = new WorkspaceDaoImpl();

    /**
     * Retrieves all workspaces from the database.
     * @return List of all workspaces
     */
    @Override
    public List<Workspace> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.workspaces;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Workspace> workspaces = new ArrayList<>();

            while (resultSet.next()) {
                workspaces.add(buildWorkspace(resultSet));
            }

            return workspaces;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a workspace by its ID from the database.
     * @param id ID of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    @Override
    public Optional<Workspace> findById(Long id){
        String sqlFindById = """
                SELECT * FROM coworking.workspaces
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildWorkspace(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a workspace by its ID from the database.
     * @param id ID of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteById(Long id){
        String sqlDeleteById = """
                DELETE FROM coworking.workspaces
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

    /**
     * Deletes all workspaces from the database.
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteAll = """
            DELETE FROM coworking.workspaces;
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteAll)) {
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves a new workspace to the database.
     * @param workspace Workspace object to save
     * @return The saved workspace object
     */
    @Override
    public Workspace save(Workspace workspace){
        String sqlSave = """
                INSERT INTO coworking.workspaces(name)
                VALUES (?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, workspace.getName());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                workspace.setId(keys.getObject("id", Long.class));
            }

            return workspace;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing workspace in the database.
     * @param workspace Workspace object with updated information
     * @return True if update was successful, false otherwise
     */
    @Override
    public boolean update(Workspace workspace) {
        String sqlUpdate = """
            UPDATE coworking.workspaces
            SET name = ?
            WHERE id = ?;
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setLong(2, workspace.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a workspace by its name from the database.
     * @param name Name of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    public Optional<Workspace> findByName(String name){
        String sqlFindByName = """
                SELECT * FROM coworking.workspaces
                WHERE name=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
            preparedStatement.setObject(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildWorkspace(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a workspace by its name from the database.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteByName(String name){
        String sqlDeleteByName = """
                DELETE FROM coworking.workspaces
                WHERE name = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteByName)) {
            preparedStatement.setObject(1, name);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Builds a Workspace object from the ResultSet.
     * @param resultSet ResultSet containing workspace data
     * @return Workspace object built from the ResultSet
     * @throws SQLException If an SQL exception occurs
     */
    private Workspace buildWorkspace(ResultSet resultSet) throws SQLException {
        return Workspace.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    /**
     * Retrieves the singleton instance of WorkspaceDaoImpl.
     * @return Singleton instance of WorkspaceDaoImpl
     */
    public static WorkspaceDaoImpl getInstance() {
        return workspaceDaoImpl;
    }
}
