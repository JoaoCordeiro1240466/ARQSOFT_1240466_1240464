package pt.psoft.g1.psoftg1.services;

import pt.psoft.g1.psoftg1.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(Long authorNumber);

    List<Author> findByName(String name);

    Author create(CreateAuthorRequest resource);

    Author partialUpdate(Long authorNumber, UpdateAuthorRequest resource);
}
