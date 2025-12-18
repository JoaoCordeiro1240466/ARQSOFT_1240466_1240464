package pt.psoft.g1.psoftg1.services;

import pt.psoft.g1.psoftg1.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByGenreId(String genreId);

    List<Book> findByAuthorId(String authorId);

    List<Book> findAll();
}
