package pt.psoft.g1.psoftg1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> findByGenreId(String genreId) {
        return bookRepository.findByGenreId(genreId);
    }

    @Override
    public List<Book> findByAuthorId(String authorId) {
        return bookRepository.findByAuthorIdsContaining(authorId);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
