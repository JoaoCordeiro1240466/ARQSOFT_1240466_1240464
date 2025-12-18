package pt.psoft.g1.psoftg1.services;

import pt.psoft.g1.psoftg1.model.Book;
import pt.psoft.g1.psoftg1.services.dtos.CreateBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.CreateCompositeBookRequest;
import pt.psoft.g1.psoftg1.services.dtos.UpdateBookRequest;

import java.util.Optional;

public interface BookService {

    Book create(CreateBookRequest request);

    Book createCompositeBook(CreateCompositeBookRequest request);

    Book update(String isbn, UpdateBookRequest request, Long currentVersion);

    Optional<Book> findByIsbn(String isbn);
}
