package pt.psoft.g1.psoftg1.genremanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenre(String genreName);
}
