package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.UserDTO;
import org.example.entity.Audit;
import org.example.entity.User;
import org.example.entity.types.ActionType;
import org.example.entity.types.AuditType;
import org.example.exceptions.UserNotFoundException;
import org.example.mappers.UserMapper;
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

    @Test
    @DisplayName("Test retrieving user by ID")
    void testGetUserById() throws UserNotFoundException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userDao.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(userId);

        assertNotNull(userDTO);
        assertEquals(userId, userDTO.getId());
        assertEquals("testUser", userDTO.getUsername());
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test user not found by id throws exception")
    void testGetUserByIdNotFound() {
        Long userId = 1L;

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test retrieving user by username")
    void testGetUserByUsername() throws UserNotFoundException {
        String username = "testUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(userDao.findByUsername(username)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(username);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Test user not found by username throws exception")
    void testGetUserByUsernameNotFound() {
        String username = "testUser";

        when(userDao.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(username));
        verify(userDao, times(1)).findByUsername(username);
    }

}
