package org.example.сontrollers;

import org.example.controllers.SecurityController;
import org.example.dto.AuthRequest;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.entity.types.Role;
import org.example.exceptions.InvalidCredentialsException;
import org.example.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Test security")
public class SecurityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityController securityController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
    }

    @Test
    @DisplayName("Registration success test")
    public void testRegister() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password");
        UserDTO mockUserDTO = UserDTO.builder().id(1L).username("testuser").role(Role.USER).build();

        when(securityService.register(request)).thenReturn(mockUserDTO);

        mockMvc.perform(post("/auth/registration")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("username").value("testuser"));
    }


    @Test
    @DisplayName("Authentication success test")
    public void testAuthenticate() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password");
        TokenResponse mockTokenResponse = new TokenResponse("mock_token");

        when(securityService.authenticate(request)).thenReturn(mockTokenResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("mock_token"));
    }

    @Test
    @DisplayName("Authentication with invalid credentials test")
    public void testAuthenticateInvalidCredentials() {
        AuthRequest authRequest = new AuthRequest("testUser", "incorrectPassword");
        when(securityService.authenticate(authRequest)).thenThrow(new InvalidCredentialsException("Incorrect username or password."));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            securityController.authenticate(authRequest);
        });

        assertEquals("Incorrect username or password.", exception.getMessage());
    }
}
