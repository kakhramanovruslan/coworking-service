package org.example.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for Data Access Objects (DAO).
 * @param <ID> Type of the entity's ID
 * @param <Entity> Entity type for the DAO
 */
public interface Dao<ID, Entity> {

    /**
     * Retrieves all entities of a specific type.
     * @return List of all entities
     */
    List<Entity> findAll();

    /**
     * Retrieves an entity by its ID.
     * @param id ID of the entity to retrieve
     * @return Optional containing the entity if found, otherwise empty
     */
    Optional<Entity> findById(ID id);

    /**
     * Deletes an entity by its ID.
     * @param id ID of the entity to delete
     * @return True if deletion was successful, false otherwise
     */
    boolean deleteById(ID id);

    /**
     * Saves or creates a new entity.
     * @param entity Entity to save or create
     * @return The saved entity
     */
    Entity save(Entity entity);

    /**
     * Updates an existing entity.
     * @param entity Entity to update
     * @return True if update was successful, false otherwise
     */
    boolean update(Entity entity);


}
