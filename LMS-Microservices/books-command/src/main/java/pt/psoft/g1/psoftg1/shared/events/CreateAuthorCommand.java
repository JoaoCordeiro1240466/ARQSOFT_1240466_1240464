package pt.psoft.g1.psoftg1.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comando enviado para a fila (RabbitMQ) para criar um autor
 * no microsserviço de People.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthorCommand {

    // Este ID geralmente é gerado antes de enviar o comando (UUID)
    // ou enviado para garantir idempotência.
    private String authorId;

    private String name;

    private String bio;
}