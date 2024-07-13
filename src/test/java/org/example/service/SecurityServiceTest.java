package org.example.service;

import org.example.dto.AuthRequest;
import org.example.exceptions.InvalidCredentialsException;
import org.example.repository.UserRepository;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.RegisterException;
import org.example.utils.JwtTokenUtil;
import org.example.utils.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private SecurityService securityService;

    @Test
    @DisplayName("Test register a new user successfully")
    void testRegisterUserSuccess() {
        String username = "newUser";
        String password = "password123";
        AuthRequest authRequest = new AuthRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User newUser = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username(username)
                .password(newUser.getPassword())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO userDTO = securityService.register(authRequest);

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
    }

    @Test
    @DisplayName("Test register a user with existing username throws RegisterException")
    void testRegisterUserWithExistingUsername() {
        String username = "existingUser";
        String password = "password123";
        AuthRequest authRequest = new AuthRequest(username, password);
        User existingUser = new User();
        existingUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(RegisterException.class, () -> securityService.register(authRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Authenticate user successfully")
    void testAuthenticateUserSuccess() {
        String username = "testUser";
        String password = "password123";
        AuthRequest authRequest = new AuthRequest(username, password);
        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(username)).thenReturn("testToken");

        TokenResponse tokenResponse = securityService.authenticate(authRequest);

        assertNotNull(tokenResponse);
        assertEquals("testToken", tokenResponse.token());
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtTokenUtil, times(1)).generateToken(username);
    }

    @Test
    @DisplayName("Authenticate user with incorrect username throws AuthenticationException")
    void testAuthenticateUserWithIncorrectUsername() {
        String username = "nonExistentUser";
        String password = "password123";
        AuthRequest authRequest = new AuthRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> securityService.authenticate(authRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Authenticate user with incorrect password throws AuthenticationException")
    void testAuthenticateUserWithIncorrectPassword() {
        String username = "testUser";
        String password = "wrongPassword";
        AuthRequest authRequest = new AuthRequest(username, password);
        String hashedPassword = PasswordUtil.hashPassword("correctPassword");
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> securityService.authenticate(authRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }
}
