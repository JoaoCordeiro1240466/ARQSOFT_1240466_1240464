package pt.psoft.g1.psoftg1.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.model.Author;
import pt.psoft.g1.psoftg1.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorCommandListener {

    private final AuthorRepository authorRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthorCommandListener.class);

    @RabbitListener(queues = RabbitMQConfig.AUTHOR_CREATE_QUEUE)
    public void handleCreateAuthorCommand(CreateAuthorCommand command) {
        Author author = new Author(command.getAuthorId(), command.getName(), command.getBio());
        authorRepository.save(author);
        logger.info("Autor criado via RabbitMQ: {}", author.getAuthorId());
    }
}
