package org.example.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for Data Access Objects (DAO).
 * @param <U> the type of the entity's identifier
 * @param <T> the type of the entity
 */
public interface Repository<U, T> {

    /**
     * Retrieves all entities of a specific type.
     * @return List of all entities
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its ID.
     * @param id ID of the entity to retrieve
     * @return Optional containing the entity if found, otherwise empty
     */
    Optional<T> findById(U id);

    /**
     * Deletes an entity by its ID.
     * @param id ID of the entity to delete
     * @return True if deletion was successful, false otherwise
     */
    boolean deleteById(U id);

    /**
     * Deletes all from table.
     * @return True if deletion was successful, false otherwise
     */
    boolean deleteAll();

    /**
     * Saves or creates a new entity.
     * @param entity Entity to save or create
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Updates an existing entity.
     * @param entity Entity to update
     * @return True if update was successful, false otherwise
     */
    boolean update(T entity);


}
