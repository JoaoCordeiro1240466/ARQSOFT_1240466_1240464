package pt.psoft.g1.psoftg1.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.model.Author;
import pt.psoft.g1.psoftg1.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.services.UpdateAuthorRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findByAuthorNumber(final Long authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Author create(final CreateAuthorRequest resource) {
        final Author author = new Author();
        author.setName(resource.getFullName());
        author.setBio(resource.getBio());
        return authorRepository.save(author);
    }

    @Override
    public Author partialUpdate(final Long authorNumber, final UpdateAuthorRequest request) {
        final var author = findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update an author that does not yet exist"));

        if (request.getName() != null) {
            author.setName(request.getName());
        }
        if (request.getBio() != null) {
            author.setBio(request.getBio());
        }

        return authorRepository.save(author);
    }



}
