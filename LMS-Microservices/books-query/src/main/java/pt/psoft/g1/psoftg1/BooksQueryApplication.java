package pt.psoft.g1.psoftg1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BooksQueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksQueryApplication.class, args);
    }

    @Bean
    public CommandLineRunner debugRabbit(RabbitAdmin admin, Queue queue) {
        return args -> {
            System.out.println("------------------------------------------------");
            System.out.println("🐰 A TENTAR DECLARAR A FILA: " + queue.getName());
            System.out.println("------------------------------------------------");

            try {
                // Força a ida ao RabbitMQ agora mesmo
                String result = admin.declareQueue(queue);

                if (result != null) {
                    System.out.println("✅ SUCESSO! Fila confirmada no RabbitMQ: " + result);
                } else {
                    System.out.println("⚠️ AVISO: O RabbitMQ devolveu null (a fila pode já existir).");
                }
            } catch (Exception e) {
                System.err.println("❌ ERRO CRÍTICO AO FALAR COM O RABBITMQ:");
                e.printStackTrace();
            }
            System.out.println("------------------------------------------------");
        };
    }
}