package org.example.service;

import org.example.dao.impl.UserDaoImpl;
import org.example.entity.User;
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
    private UserDaoImpl userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterNewUserReturnsTrue() {
        String username = "newuser";
        String password = "password";
        when(userDao.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean result = userService.register(username, password);

        assertTrue(result);

        verify(userDao).save(any(User.class));
        verify(userDao).findByUsername(anyString());
    }

    @Test
    void testRegisterExistingUserReturnsFalse() {
        String username = "existinguser";
        String password = "password";
        when(userDao.findByUsername(username)).thenReturn(Optional.of(new User()));

        boolean result = userService.register(username, password);

        assertFalse(result);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateValidCredentialsReturnsTrue() {
        String username = "user";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticate(username, password);

        assertTrue(result);
    }

    @Test
    void testAuthenticateInvalidCredentialsReturnsFalse() {
        String username = "user";
        String password = "wrongpassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.authenticate(username, password);

        assertFalse(result);
    }

    @Test
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
    void testGetUserNonExistingUserReturnsEmpty() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(id);

        assertTrue(result.isEmpty());
    }

    @Test
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
    void testGetUserByUsernameNonExistingUserReturnsEmpty() {
        String username = "nonexistinguser";
        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(username);

        assertTrue(result.isEmpty());
    }
}