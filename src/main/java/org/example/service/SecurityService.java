package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.dto.AuthRequest;
import org.example.exceptions.InvalidCredentialsException;
import org.example.repository.UserRepository;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.entity.types.ActionType;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.RegisterException;
import org.example.mappers.UserMapper;
import org.example.utils.JwtTokenUtil;
import org.example.utils.PasswordUtil;
import org.example.utils.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for security of app.
 */
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param request the auth request
     * @return the registered user DTO
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws RegisterException if a user with the same login already exists
     */
    @Auditable(actionType = ActionType.REGISTRATION)
    public UserDTO register(AuthRequest request) {
        ValidationUtil.validate(request);

        String username = request.username();
        String password = request.password();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new RegisterException("A user with this username already exists.");
        }

        User newUser = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .build();

        User savedUser = userRepository.save(newUser);
        return UserMapper.INSTANCE.toDTO(savedUser);
    }

    /**
     * Authenticate a user with the provided username and password.
     *
     * @param request the auth request
     * @return a token response containing the generated JWT token
     * @throws InvalidCredentialsException if the user is not found or the password is incorrect
     */
    @Auditable(actionType = ActionType.AUTHORIZATION)
    public TokenResponse authenticate(AuthRequest request) {
        String username = request.username();
        String password = request.password();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty() || !PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            throw new InvalidCredentialsException("Incorrect username or password.");
        }

        String token = jwtTokenUtil.generateToken(username);
        return new TokenResponse(token);
    }
}