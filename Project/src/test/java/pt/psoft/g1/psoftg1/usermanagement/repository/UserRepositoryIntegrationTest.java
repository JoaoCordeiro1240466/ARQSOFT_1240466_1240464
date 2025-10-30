package pt.psoft.g1.psoftg1.usermanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles({"base65", "sql"})
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Create and save test users
        user1 = User.newUser("john@example.com", "Password123!", "John Doe");
        user1.setId("USR-001");
        user1 = userRepository.save(user1);

        user2 = User.newUser("alice@example.com", "Password123!", "Alice Wonderland");
        user2.setId("USR-002");
        user2 = userRepository.save(user2);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // when
        Optional<User> result = userRepository.findByUsername("john@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john@example.com");
        assertThat(result.get().getName()).isNotNull();
        assertThat(result.get().getName().toString()).contains("John");
    }

    @Test
    public void whenFindById_thenReturnUser() {
        // when
        Optional<User> result = userRepository.findById(user1.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(user1.getId());
        assertThat(result.get().getName().toString()).contains("John");
    }

    @Test
    public void whenFindById_NotFound_thenReturnEmpty() {
        // when
        Optional<User> result = userRepository.findById("NON-EXISTENT-ID");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void whenSave_thenUserIsPersisted() {
        // given
        User newUser = User.newUser("charlie@example.com", "Password123!", "Charlie Brown");

        // when
        User savedUser = userRepository.save(newUser);

        // then
        assertThat(savedUser.getId()).isNotNull();
        Optional<User> found = userRepository.findById(savedUser.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("charlie@example.com");
        assertThat(found.get().getName().toString()).contains("Charlie");
    }

    @Test
    public void whenFindByNameName_thenReturnUsers() {
        // when
        List<User> users = userRepository.findByNameName("John Doe");

        // then
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName().toString()).contains("John Doe");
    }

    @Test
    public void whenFindByNameNameContains_thenReturnUsers() {
        // when
        List<User> users = userRepository.findByNameNameContains("John");

        // then
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName().toString()).contains("John");
    }
}

