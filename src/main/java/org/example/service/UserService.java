package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.dao.impl.UserDaoImpl;
import org.example.entity.User;
import org.example.utils.ConnectionManager;

import java.util.Optional;


/**
 * Service class for managing users.
 */
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public UserService(ConnectionManager connectionManager) {
        this.userDao = new UserDaoImpl(connectionManager);
    }

    /**
     * Registers a new user with the provided username and password.
     * @param username Username of the new user
     * @param password Password of the new user
     * @return True if registration is successful, false otherwise.
     */
    public boolean register(String username, String password){
        Optional<User> existingUser = userDao.findByUsername(username);
        if(existingUser.isEmpty()){
            userDao.save(User.builder()
                            .username(username)
                            .password(password)
                            .build());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Authenticates a user with the provided username and password.
     * @param username Username of the user to authenticate
     * @param password Password of the user to authenticate
     * @return True if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password){
        Optional<User> user = userDao.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password))
            return true;
        else
            return false;
    }

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
