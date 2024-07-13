package org.example.service;

import org.example.repository.UserRepository;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.UserNotFoundException;
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
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test retrieving user by ID")
    void testGetUserById() throws UserNotFoundException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(userId);

        assertNotNull(userDTO);
        assertEquals(userId, userDTO.getId());
        assertEquals("testUser", userDTO.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test user not found by id throws exception")
    void testGetUserByIdNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test retrieving user by username")
    void testGetUserByUsername() throws UserNotFoundException {
        String username = "testUser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(username);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Test user not found by username throws exception")
    void testGetUserByUsernameNotFound() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

}
