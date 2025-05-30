package com.example.securityapp.securityapp.controller;

import com.example.securityapp.securityapp.model.UserEntity;
import com.example.securityapp.securityapp.service.JWTService;
import com.example.securityapp.securityapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;
    @Mock
    private JWTService jwtService;

    private final MockMvc mockMvc;

    public AuthControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void shouldReturnJwtTokenOnValidLogin() throws Exception {
        UserEntity mockUser = new UserEntity("testuser", "irrelevant", Set.of("USER"));
        Mockito.when(userService.authenticate(any(), any())).thenReturn(mockUser);
        Mockito.when(jwtService.generateToken(mockUser)).thenReturn("fake.jwt.token");

        String json = """
            {
              "username": "testuser",
              "password": "admin123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andDo(print());
    }
}
