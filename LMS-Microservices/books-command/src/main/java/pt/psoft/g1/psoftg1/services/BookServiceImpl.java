package pt.psoft.g1.psoftg1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.model.Description;
import pt.psoft.g1.psoftg1.model.Isbn;
import pt.psoft.g1.psoftg1.model.Title;
import pt.psoft.g1.psoftg1.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.services.dtos.CreateBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.UpdateBookRequest;
import pt.psoft.g1.psoftg1.shared.events.BookCreatedEvent;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Book create(CreateBookRequest request) {
        final String isbnStr = request.getIsbn();
        if (bookRepository.findByIsbn(isbnStr).isPresent()) {
            throw new ConflictException("Book with ISBN " + isbnStr + " already exists");
        }

        Book newBook = new Book();
        newBook.setIsbn(new Isbn(request.getIsbn()));
        newBook.setTitle(new Title(request.getTitle()));
        newBook.setDescription(new Description(request.getDescription()));
        newBook.setGenreId(request.getGenreId());
        newBook.setAuthorIds(request.getAuthorIds());

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
        rabbitTemplate.convertAndSend("library-events-exchange", "books.created", event);
        System.out.println("EVENTO PUBLICADO"); // Debug message

        return savedBook;
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
