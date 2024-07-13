package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.entity.types.ActionType;
import org.example.repository.UserRepository;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.UserNotFoundException;
import org.example.mappers.UserMapper;
import org.springframework.stereotype.Service;


import javax.servlet.ServletContext;
import java.util.Optional;


/**
 * Service class for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param id ID of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     * @throws UserNotFoundException if no user is found with the specified ID
     */
    public UserDTO getUser(Long id) throws UserNotFoundException{
        Optional<User> foundedUser = userRepository.findById(id);
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
        Optional<User> foundedUser = userRepository.findByUsername(username);
        if(foundedUser.isEmpty()) throw new UserNotFoundException("User with this username does not exist.");
        return UserMapper.INSTANCE.toDTO(foundedUser.get());
    }

    @Auditable(actionType = ActionType.LOGOUT)
    public void logout(ServletContext servletContext){
        servletContext.removeAttribute("authentication");
    }
}
