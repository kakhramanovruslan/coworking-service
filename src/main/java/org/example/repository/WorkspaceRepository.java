package org.example.repository;

import org.example.entity.Workspace;

import java.util.Optional;

/**
 * Interface for Workspace Data Access Object (DAO).
 * Extends the base Repository interface for generic CRUD operations.
 * Additional method to find a workspace by its name.
 */
public interface WorkspaceRepository extends Repository<Long, Workspace> {

    /**
     * Finds a workspace by its name.
     *
     * @param name the name of the workspace to find
     * @return an Optional containing the found workspace, or an empty Optional if no workspace is found
     */
    Optional<Workspace> findByName(String name);

    /**
     * Deletes a workspace by its name from the database.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    boolean deleteByName(String name);
}
