package org.example.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.repository.WorkspaceRepository;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of WorkspaceRepository interface for interacting with Workspace entities in the database.
 */
@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final ConnectionManager connectionManager;

    /**
     * Retrieves all workspaces from the database.
     * @return List of all workspaces
     */
    @Override
    public List<Workspace> findAll(){
        String sqlFindAll = """
                SELECT * FROM coworking.workspaces;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Workspace> workspaces = new ArrayList<>();

            while (resultSet.next()) {
                workspaces.add(buildWorkspace(resultSet));
            }

            return workspaces;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
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

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildWorkspace(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
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
     * Deletes all workspaces from the database.
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteAll = """
            DELETE FROM coworking.workspaces;
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
     * Saves a new workspace to the database.
     * @param workspace Workspace object to save
     * @return The saved workspace object
     */
    @Override
    public Workspace save(Workspace workspace) {
        String sqlSave = """
        INSERT INTO coworking.workspaces(name)
        SELECT ?
        WHERE NOT EXISTS (
            SELECT 1
            FROM coworking.workspaces
            WHERE name = ?
        );
        """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setString(2, workspace.getName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new WorkspaceAlreadyExistException("Workspace with this name already exists.");
            }

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                workspace.setId(keys.getObject("id", Long.class));
            }

            return workspace;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
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

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setLong(2, workspace.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a workspace by its name from the database.
     * @param name Name of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    @Override
    public Optional<Workspace> findByName(String name){
        String sqlFindByName = """
                SELECT * FROM coworking.workspaces
                WHERE name=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
            preparedStatement.setObject(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildWorkspace(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a workspace by its name from the database.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteByName(String name){
        String sqlDeleteByName = """
                DELETE FROM coworking.workspaces
                WHERE name = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteByName)) {
            preparedStatement.setObject(1, name);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error during execution of SQL query: " + e.getMessage());
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

}
