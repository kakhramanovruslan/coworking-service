package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.dto.TokenResponse;
import org.example.entity.User;
import org.example.exceptions.AuthenticationException;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.RegisterException;
import org.example.utils.JwtTokenUtil;

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
    public User register(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new RegisterException("Пользователь с таким логином уже существует.");
        }

        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();

        return userDao.save(newUser);
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

        if (!optionalUser.get().getPassword().equals(password)) {
            throw new AuthenticationException("Неверный пароль.");
        }

        String token = jwtTokenUtil.generateToken(username);
        return new TokenResponse(token);
    }
}