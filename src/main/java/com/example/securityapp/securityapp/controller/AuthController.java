package com.example.securityapp.securityapp.controller;

import com.example.securityapp.securityapp.dto.LoginRequest;
import com.example.securityapp.securityapp.dto.LoginResponse;
import com.example.securityapp.securityapp.model.UserEntity;
import com.example.securityapp.securityapp.service.JWTService;
import com.example.securityapp.securityapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JWTService jwtService;

    public AuthController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserEntity user = userService.authenticate(
                request.getUsername(),
                request.getPassword()
        );
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
