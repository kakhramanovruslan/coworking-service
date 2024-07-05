package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
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
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

//    @Test
//    @DisplayName("Test register new user returns true")
//    void testRegisterNewUserReturnsTrue() {
//        String username = "newUser";
//        String password = "password";
//        when(userDao.findByUsername(anyString())).thenReturn(Optional.empty());
//
//        boolean result = userService.register(username, password);
//
//        assertTrue(result);
//
//        verify(userDao).save(any(User.class));
//        verify(userDao).findByUsername(anyString());
//    }
//
//    @Test
//    @DisplayName("Test register existing user returns false")
//    void testRegisterExistingUserReturnsFalse() {
//        String username = "existingUser";
//        String password = "password";
//        when(userDao.findByUsername(username)).thenReturn(Optional.of(new User()));
//
//        boolean result = userService.register(username, password);
//
//        assertFalse(result);
//        verify(userDao, never()).save(any(User.class));
//    }
//
//    @Test
//    @DisplayName("Test authenticate with valid credentials returns true")
//    void testAuthenticateValidCredentialsReturnsTrue() {
//        String username = "user";
//        String password = "password";
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
//
//        boolean result = userService.authenticate(username, password);
//
//        assertTrue(result);
//    }
//
//    @Test
//    @DisplayName("Test authenticate with invalid credentials returns false")
//    void testAuthenticateInvalidCredentialsReturnsFalse() {
//        String username = "user";
//        String password = "wrongPassword";
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("password");
//        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));
//
//        boolean result = userService.authenticate(username, password);
//
//        assertFalse(result);
//    }

    @Test
    @DisplayName("Test get user by ID returns existing user")
    void testGetUserExistingUserReturnsUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userDao.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Test get user by ID returns empty for non-existing user")
    void testGetUserNonExistingUserReturnsEmpty() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test get user by username returns existing user")
    void testGetUserByUsernameExistingUserReturnsUser() {
        String username = "user";
        User user = new User();
        user.setUsername(username);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Test get user by username returns empty for non-existing user")
    void testGetUserByUsernameNonExistingUserReturnsEmpty() {
        String username = "nonExistingUser";
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(username);

        assertTrue(result.isEmpty());
    }
}
