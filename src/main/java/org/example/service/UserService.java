package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.entity.User;


import java.util.Optional;


/**
 * Service class for managing users.
 */
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /**
     * Retrieves a user by their ID.
     * @param id ID of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    public Optional<User> getUser(Long id){
        return userDao.findById(id);
    }

    /**
     * Retrieves a user by their username.
     * @param username Username of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    public Optional<User> getUser(String username){
        return userDao.findByUsername(username);
    }

}
