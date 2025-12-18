package pt.psoft.g1.psoftg1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.services.dtos.CreateBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.CreateCompositeBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.UpdateBookRequest;
import pt.psoft.g1.psoftg1.shared.events.BookCreatedEvent;
import pt.psoft.g1.psoftg1.shared.events.CreateAuthorCommand;
import pt.psoft.g1.psoftg1.shared.events.CreateGenreCommand;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.exchange.people}")
    private String peopleExchange;

    @Value("${rabbit.exchange.operations}")
    private String operationsExchange;

    @Value("${rabbit.exchange.library}")
    private String libraryEventsExchange;

    @Override
    public Book create(CreateBookRequest request) {
        final String isbnStr = request.getIsbn();
        if (bookRepository.findByIsbn(isbnStr).isPresent()) {
            throw new ConflictException("Book with ISBN " + isbnStr + " already exists");
        }

        Book newBook = new Book(
                request.getIsbn(),
                request.getTitle(),
                request.getDescription(),
                request.getGenreId(),
                request.getAuthorIds()
        );

        Book savedBook = bookRepository.save(newBook);

        // Publish event
        String authorId = (savedBook.getAuthorIds() != null && !savedBook.getAuthorIds().isEmpty())
                ? savedBook.getAuthorIds().get(0)
                : null;

        String description = savedBook.getDescription() != null ? savedBook.getDescription().toString() : "";

        BookCreatedEvent event = new BookCreatedEvent(
                savedBook.getIsbn().toString(),
                savedBook.getTitle().toString(),
                description,
                authorId,
                savedBook.getGenreId()
        );
        rabbitTemplate.convertAndSend(libraryEventsExchange, "books.created", event);
        System.out.println("EVENTO PUBLICADO"); // Debug message

        return savedBook;
    }

    @Override
    public Book createCompositeBook(CreateCompositeBookRequest request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new ConflictException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        String authorId = UUID.randomUUID().toString();
        CreateAuthorCommand authorCommand = new CreateAuthorCommand(
                authorId,
                request.getAuthor().getName(),
                request.getAuthor().getBio()
        );
        rabbitTemplate.convertAndSend(peopleExchange, "authors.create", authorCommand);

        String genreId = UUID.randomUUID().toString();
        CreateGenreCommand genreCommand = new CreateGenreCommand(genreId, request.getGenre());
        rabbitTemplate.convertAndSend(operationsExchange, "genres.create", genreCommand);

        Book newBook = new Book(
                request.getIsbn(),
                request.getTitle(),
                request.getDescription(),
                genreId,
                Collections.singletonList(authorId)
        );

        return bookRepository.save(newBook);
    }

    @Override
    public Book update(String isbn, UpdateBookRequest request, Long currentVersion) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book with ISBN " + isbn + " not found"));

        book.applyPatch(currentVersion, request);

        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
