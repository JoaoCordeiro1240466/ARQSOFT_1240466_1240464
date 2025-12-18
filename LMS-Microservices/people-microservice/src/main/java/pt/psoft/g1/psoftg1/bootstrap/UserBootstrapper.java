package pt.psoft.g1.psoftg1.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.services.UserService;
import pt.psoft.g1.psoftg1.services.UserService.CreateUserRequest;

@Component
@RequiredArgsConstructor
public class UserBootstrapper implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o user já existe para não dar erro de duplicado
        if (userService.findByUsername("admin@library.pt").isEmpty()) {

            // Cria o user com Password segura
            CreateUserRequest admin = new CreateUserRequest(
                    "admin@library.pt",
                    "Password123!",  // Atenção às regras da tua classe Password (Maiúscula, número, etc)
                    "Administrador do Sistema"
            );

            userService.create(admin);
            System.out.println("✅ USER DE TESTE CRIADO: admin@library.pt / Password123!");
        }
    }
}