package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.AuthenticationException;
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
    private UserDao userDao;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private SecurityService securityService;

    @Test
    @DisplayName("Test register a new user successfully")
    void testRegisterUserSuccess() {
        String username = "newUser";
        String password = "password123";

        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        User newUser = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username(username)
                .password(newUser.getPassword())
                .build();

        when(userDao.save(any(User.class))).thenReturn(savedUser);

        UserDTO userDTO = securityService.register(username, password);

        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, times(1)).save(any(User.class));

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
    }

    @Test
    @DisplayName("Test register a user with existing username throws RegisterException")
    void testRegisterUserWithExistingUsername() {
        String username = "existingUser";
        String password = "password123";
        User existingUser = new User();
        existingUser.setUsername(username);

        when(userDao.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(RegisterException.class, () -> securityService.register(username, password));
        verify(userDao, times(1)).findByUsername(username);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Authenticate user successfully")
    void testAuthenticateUserSuccess() {
        String username = "testUser";
        String password = "password123";
        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .build();

        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(username)).thenReturn("testToken");

        TokenResponse tokenResponse = securityService.authenticate(username, password);

        assertNotNull(tokenResponse);
        assertEquals("testToken", tokenResponse.token());
        verify(userDao, times(1)).findByUsername(username);
        verify(jwtTokenUtil, times(1)).generateToken(username);
    }

    @Test
    @DisplayName("Authenticate user with incorrect username throws AuthenticationException")
    void testAuthenticateUserWithIncorrectUsername() {
        String username = "nonExistentUser";
        String password = "password123";

        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> securityService.authenticate(username, password));
        verify(userDao, times(1)).findByUsername(username);
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Authenticate user with incorrect password throws AuthenticationException")
    void testAuthenticateUserWithIncorrectPassword() {
        String username = "testUser";
        String password = "wrongPassword";
        String hashedPassword = PasswordUtil.hashPassword("correctPassword");
        User user = User.builder()
                .username(username)
                .password(hashedPassword)
                .build();

        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> securityService.authenticate(username, password));
        verify(userDao, times(1)).findByUsername(username);
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }
}
