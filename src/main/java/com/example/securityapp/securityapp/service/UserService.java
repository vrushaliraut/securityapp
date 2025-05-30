package com.example.securityapp.securityapp.service;

import com.example.securityapp.securityapp.model.UserEntity;
import com.example.securityapp.securityapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity authenticate(String username, String rawPassword){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("Invalid username and password");
        }

        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(rawPassword,user.getPassword())){
            throw new RuntimeException("Invalid username or password");
        }
        return user; // ✅ Validated user returned for JWT generation
    }
}
