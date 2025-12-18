package pt.psoft.g1.psoftg1.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.repositories.BookRepository;
import pt.psoft.g1.psoftg1.shared.events.BookCreatedEvent;
import pt.psoft.g1.psoftg1.shared.events.BookDeletedEvent;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BookEventHandler {

    private final BookRepository bookRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleBookCreated(BookCreatedEvent event) {
        System.out.println("Received event: " + event.getTitle());

        Book book = new Book();
        book.setIsbn(event.getIsbn());
        book.setTitle(event.getTitle());
        book.setDescription(event.getDescription());
        book.setGenreId(event.getGenreId());
        // The Book entity expects a List<String> for authorIds
        book.setAuthorIds(Collections.singletonList(event.getAuthorId()));
        book.setVersion(0L); // Initial version

        bookRepository.save(book);

        System.out.println("Book saved to query database: " + book.getTitle());
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.DELETED_QUEUE_NAME)
    public void handleBookDeleted(BookDeletedEvent event) {
        System.out.println("Received book deleted event for ISBN: " + event.getIsbn());
        bookRepository.deleteByIsbn(event.getIsbn());
        System.out.println("Book with ISBN " + event.getIsbn() + " deleted from query database.");
    }
}
