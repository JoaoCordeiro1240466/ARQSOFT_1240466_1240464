package pt.psoft.g1.psoftg1.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.shared.events.AuthorValidatedEvent;
import pt.psoft.g1.psoftg1.shared.events.BookCreatedEvent;

@Service
public class BookEventsListener {

    private static final Logger logger = LoggerFactory.getLogger(BookEventsListener.class);
    private static final String EXCHANGE = "library-events-exchange";
    private static final String ROUTING_KEY = "authors.validated";

    private final AuthorRepository authorRepository;
    private final RabbitTemplate rabbitTemplate;

    public BookEventsListener(AuthorRepository authorRepository, RabbitTemplate rabbitTemplate) {
        this.authorRepository = authorRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "people-validation-queue")
    public void handleBookCreated(BookCreatedEvent event) {
        logger.info("Received book created event for ISBN: {}", event.getIsbn());

        if (event.getAuthorId() == null) {
            logger.error("Author ID is null for ISBN: {}. Cannot validate.", event.getIsbn());
            // We could send a validation failed event here as well if needed
            return;
        }

        try {
            Long authorIdLong = Long.parseLong(event.getAuthorId());

            boolean authorExists = authorRepository.findById(authorIdLong).isPresent();

            if (authorExists) {
                logger.info("✅ SAGA SUCCESS: Author with ID {} found. Book {} is approved.", event.getAuthorId(), event.getIsbn());
                AuthorValidatedEvent validationEvent = new AuthorValidatedEvent(event.getIsbn(), true, "Author found.");
                rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, validationEvent);
            } else {
                logger.error("❌ SAGA FAILURE: Author with ID {} not found! Book {} should be rejected.", event.getAuthorId(), event.getIsbn());
                AuthorValidatedEvent validationEvent = new AuthorValidatedEvent(event.getIsbn(), false, "Author not found.");
                rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, validationEvent);
            }
        } catch (NumberFormatException e) {
            logger.error("⚠️ SAGA ERROR: Author ID '{}' is not a valid number!", event.getAuthorId());
            AuthorValidatedEvent validationEvent = new AuthorValidatedEvent(event.getIsbn(), false, "Invalid Author ID format.");
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, validationEvent);
        }
    }
}
