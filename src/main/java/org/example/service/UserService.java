package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.UserNotFoundException;
import org.example.mappers.UserMapper;


import java.util.Optional;


/**
 * Service class for managing users.
 */
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /**
     * Retrieves a user by their ID.
     *
     * @param id ID of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     * @throws UserNotFoundException if no user is found with the specified ID
     */
    public UserDTO getUser(Long id) throws UserNotFoundException{
        Optional<User> foundedUser = userDao.findById(id);
        if(foundedUser.isEmpty()) throw new UserNotFoundException("User with this ID does not exist.");
        return UserMapper.INSTANCE.toDTO(foundedUser.get());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username Username of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     * @throws UserNotFoundException if no user is found with the specified username
     */
    public UserDTO getUser(String username) throws UserNotFoundException{
        Optional<User> foundedUser = userDao.findByUsername(username);
        if(foundedUser.isEmpty()) throw new UserNotFoundException("User with this username does not exist.");
        return UserMapper.INSTANCE.toDTO(foundedUser.get());
    }

}
