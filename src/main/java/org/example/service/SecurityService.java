package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotations.Loggable;
import org.example.dao.UserDao;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
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
     * @param username    the user's login
     * @param password the user's password
     * @return the registered user
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws RegisterException         if a user with the same login already exists
     */
    public UserDTO register(String username, String password) {

        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new RegisterException("Пользователь с таким логином уже существует.");
        }

        User newUser = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .build();

        User savedUser = userDao.save(newUser);
        return UserMapper.INSTANCE.toDTO(savedUser);
    }

    /**
     * Authorizes a user with the provided login and password.
     *
     * @param username    the user's login
     * @param password the user's password
     * @return an optional containing the authorized user, or empty if authentication fails
     * @throws AuthenticationException if the user is not found or the password is incorrect
     */
    public TokenResponse authenticate(String username, String password) {
        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("Пользователь с данным логином отсутствует в базе данных.");
        }

        if (!PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            throw new AuthenticationException("Неправильное имя пользователя или пароль");
        }

        String token = jwtTokenUtil.generateToken(username);
        return new TokenResponse(token);
    }
}