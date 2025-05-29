package com.example.securityapp.securityapp.repository;

import com.example.securityapp.securityapp.model.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and fetch user with roles")
    void shouldSaveAndFetchUserWithRoles() {
        //Given
        Set<String> roles = Set.of("USER", "ADMIN");
        UserEntity userEntity = new UserEntity("testuser", "hashed-password", roles);

        //When
        userRepository.save(userEntity);

        UserEntity fetched = userRepository.findByUsername("testuser").orElseThrow();

        // Then
        assertThat(fetched.getId()).isNotNull();
        assertThat(fetched.getUsername()).isEqualTo("testuser");
        assertThat(fetched.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    @DisplayName("Should enforce unique constraint on username")
    void shouldEnforceUniqueUsername() {
        // Given
        Set<String> user = Set.of("User");
        Set<String> admin = Set.of("ADMIN");
        userRepository.save(new UserEntity("duplicateUser", "pw1", user));

        // When/Then
        Exception ex = assertThrows(Exception.class, () ->
                userRepository.saveAndFlush(new UserEntity("duplicateUser", "pw2", admin))
        );
        assertThat(ex.getMessage()).contains("ConstraintViolationException");
    }

    @Test
    @DisplayName("Should load user from data.sql with roles")
    void shouldLoadSeedUserFromDataSql() {
        UserEntity user = userRepository.findByUsername("admin").orElseThrow();

        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getPassword()).startsWith("$2a$10$"); // bcrypt hash
        assertThat(user.getRoles()).containsExactlyInAnyOrder("ADMIN", "USER");
    }
}