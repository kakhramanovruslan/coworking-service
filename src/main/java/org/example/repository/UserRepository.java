package org.example.repository;

import org.example.entity.User;

import java.util.Optional;

/**
 * Interface for User Data Access Object (DAO).
 * Extends the base Repository interface for generic CRUD operations.
 * Additional method to find a user by their username.
 */
public interface UserRepository extends Repository<Long, User> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found user, or an empty Optional if no user is found
     */
    Optional<User> findByUsername(String username);
}
