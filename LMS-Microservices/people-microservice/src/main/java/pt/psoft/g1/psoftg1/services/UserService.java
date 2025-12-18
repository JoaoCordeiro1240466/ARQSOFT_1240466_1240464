package pt.psoft.g1.psoftg1.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.model.User;
import pt.psoft.g1.psoftg1.repositories.UserRepository;
import pt.psoft.g1.psoftg1.shared.events.UserCreatedEvent; // Import UserCreatedEvent
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class); // Add logger

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate; // Inject RabbitTemplate

    public User getUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public User create(final CreateUserRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        // A lógica de Roles/Authorities deve ser tratada aqui se necessário
        // Ex: user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(Collectors.toSet()));

        User savedUser = userRepo.save(user); // Save the user first to get the ID

        // Create and send UserCreatedEvent
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getFullName()
        );
        rabbitTemplate.convertAndSend("people-exchange", "user.created", userCreatedEvent);
        log.info("Event sent: UserCreatedEvent for user ID {}", savedUser.getId());


        return savedUser;
    }

    @Transactional
    public User update(final Long id, final EditUserRequest request) {
        final User user = getUser(id);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        // A lógica de update de Roles/Authorities pode ser adicionada aqui

        return userRepo.save(user);
    }

    @Transactional
    public User delete(final Long id) {
        final User user = getUser(id);
        user.setEnabled(false);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public Optional<User> findByUsername(final String username) {
        return userRepo.findByUsername(username).filter(User::isEnabled);
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("User is not logged in");
        }

        String loggedUsername = jwt.getClaimAsString("sub");

        return findByUsername(loggedUsername)
                .orElseThrow(() -> new AccessDeniedException("User not found or disabled"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUserRequest {
        private String username;
        private String password;
        private String fullName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditUserRequest {
        private String fullName;
    }
}
