package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.lendingmanagement.model.BookReplica;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.BookReplicaRepository;
import pt.psoft.g1.psoftg1.shared.events.BookCreatedEvent;
import pt.psoft.g1.psoftg1.shared.events.BookDeletedEvent;

@Service
@RequiredArgsConstructor
public class BookReplicaListener {

    private final BookReplicaRepository bookReplicaRepository;

    @RabbitListener(queues = "lending-book-created-queue")
    public void handleBookCreated(BookCreatedEvent event) {
        BookReplica bookReplica = new BookReplica(
                event.getIsbn(),
                event.getTitle(),
                event.getAuthorId(),
                event.getGenre(),
                event.getPublicationYear()
        );
        bookReplicaRepository.save(bookReplica);
    }

    @RabbitListener(queues = "lending-book-deleted-queue")
    public void handleBookDeleted(BookDeletedEvent event) {
        bookReplicaRepository.deleteById(event.getIsbn());
    }
}
