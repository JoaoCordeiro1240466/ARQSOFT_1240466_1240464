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
        // CORREÇÃO: Verificar se o 'admin2' existe, não o 'admin'
        if (userService.findByUsername("admin2@library.pt").isEmpty()) {

            CreateUserRequest admin = new CreateUserRequest(
                    "admin2@library.pt", // O email que vais usar no login
                    "Password123!",
                    "Administrador do Sistema"
            );

            userService.create(admin);
            // Atualizei o log para refletir a realidade
            System.out.println("✅ USER DE TESTE CRIADO: admin2@library.pt / Password123!");
        } else {
            System.out.println("ℹ️ O user admin2@library.pt já existe. Bootstrapper ignorado.");
        }
    }
}