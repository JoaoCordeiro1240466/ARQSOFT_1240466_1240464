package pt.psoft.g1.psoftg1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByAuthorNumber(Long authorNumber);

    List<Author> findByNameContainingIgnoreCase(String name);
}
