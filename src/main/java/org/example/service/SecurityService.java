package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotations.Auditable;
import org.example.annotations.Loggable;
import org.example.dao.UserDao;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.entity.types.ActionType;
import org.example.exceptions.AuthenticationException;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.RegisterException;
import org.example.mappers.UserMapper;
import org.example.utils.JwtTokenUtil;
import org.example.utils.PasswordUtil;

import java.util.Optional;


/**
 * Service for security of app.
 */
@RequiredArgsConstructor
public class SecurityService {

    private final UserDao userDao;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param username the user's login
     * @param password the user's password
     * @return the registered user DTO
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws RegisterException if a user with the same login already exists
     */
    @Auditable(actionType = ActionType.REGISTRATION)
    public UserDTO register(String username, String password) {

        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new RegisterException("A user with this username already exists.");
        }

        User newUser = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .build();

        User savedUser = userDao.save(newUser);
        return UserMapper.INSTANCE.toDTO(savedUser);
    }

    /**
     * Authenticate a user with the provided username and password.
     *
     * @param username the user's name
     * @param password the user's password
     * @return a token response containing the generated JWT token
     * @throws AuthenticationException if the user is not found or the password is incorrect
     */
    @Auditable(actionType = ActionType.AUTHORIZATION)
    public TokenResponse authenticate(String username, String password) {
        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("User with this username does not exist.");
        }

        if (!PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            throw new AuthenticationException("Incorrect username or password.");
        }

        String token = jwtTokenUtil.generateToken(username);
        return new TokenResponse(token);
    }
}