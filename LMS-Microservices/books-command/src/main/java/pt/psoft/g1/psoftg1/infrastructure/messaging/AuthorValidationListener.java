package pt.psoft.g1.psoftg1.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.repositories.BookRepository;
import pt.psoft.g1.psoftg1.shared.events.AuthorValidatedEvent;
import pt.psoft.g1.psoftg1.shared.events.BookDeletedEvent;

@Service
@RequiredArgsConstructor
public class AuthorValidationListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthorValidationListener.class);

    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "books-author-validation-queue")
    public void handleAuthorValidation(AuthorValidatedEvent event) {
        if (!event.isValid()) {
            logger.error("Author validation failed for book with ISBN: {}. Deleting book.", event.getIsbn());
            bookRepository.deleteByIsbn(event.getIsbn());

            BookDeletedEvent deletedEvent = new BookDeletedEvent(event.getIsbn());
            rabbitTemplate.convertAndSend("library-events-exchange", "books.deleted", deletedEvent);
            logger.info("Sent BookDeletedEvent for ISBN: {}", event.getIsbn());

        } else {
            // Optional: Check if the book still exists before logging success
            bookRepository.findByIsbn(event.getIsbn()).ifPresent(book -> {
                logger.info("Author validation successful for book with ISBN: {}. Book is confirmed.", event.getIsbn());
            });
        }
    }
}
