package pt.psoft.g1.psoftg1.usermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void ensureUserIdIsStringAndCanBeSet() {
        // Arrange
        User user = new User("user@example.com", "Password1!");
        String expectedId = "USR-001";

        // Act
        user.setId(expectedId);

        // Assert
        assertThat(user.getId()).isEqualTo(expectedId);
    }

    @Test
    void ensurePasswordIsEncodedWhenSet() {
        // Arrange
        String rawPassword = "Secret123!";
        User user = new User("user@example.com", rawPassword);

        // Act
        String encoded = user.getPassword();

        // Assert
        assertThat(encoded).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
    }

    @Test
    void ensureFactoryMethodCreatesUserWithName() {
        // Act
        User user = User.newUser("user@example.com", "Password1!", "John Doe");

        // Assert
        assertThat(user.getUsername()).isEqualTo("user@example.com");
        assertThat(user.getName()).isNotNull();
        assertThat(user.getName().toString()).contains("John Doe");
    }

    @Test
    void ensureFactoryMethodCreatesUserWithRole() {
        // Act
        User user = User.newUser("user@example.com", "Password1!", "John Doe", "ADMIN");

        // Assert
        assertThat(user.getAuthorities())
                .extracting(Role::getAuthority)
                .contains("ADMIN");
    }

    @Test
    void ensureAddAuthorityAddsRoleCorrectly() {
        // Arrange
        User user = new User("user@example.com", "Password1!");
        Role role = new Role("USER");

        // Act
        user.addAuthority(role);

        // Assert
        assertThat(user.getAuthorities()).contains(role);
    }

    @Test
    void ensureIsAccountNonExpiredReturnsEnabled() {
        User user = new User("user@example.com", "Password1!");
        user.setEnabled(true);

        assertThat(user.isAccountNonExpired()).isTrue();

        user.setEnabled(false);
        assertThat(user.isAccountNonExpired()).isFalse();
    }

    @Test
    void ensureIsAccountNonLockedReturnsEnabled() {
        User user = new User("user@example.com", "Password1!");
        user.setEnabled(false);
        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    void ensureIsCredentialsNonExpiredReturnsEnabled() {
        User user = new User("user@example.com", "Password1!");
        user.setEnabled(true);
        assertThat(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void ensureSetNameCreatesNameValueObject() {
        User user = new User("user@example.com", "Password1!");
        user.setName("Alice Wonderland");

        assertThat(user.getName()).isNotNull();
        assertThat(user.getName().toString()).contains("Alice Wonderland");
    }
}
