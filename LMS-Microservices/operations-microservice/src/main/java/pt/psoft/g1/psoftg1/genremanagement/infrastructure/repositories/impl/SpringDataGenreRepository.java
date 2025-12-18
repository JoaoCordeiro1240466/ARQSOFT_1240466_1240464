package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.Optional;

public interface SpringDataGenreRepository extends GenreRepository {

    @Override
    @Query("SELECT g FROM Genre g WHERE g.genre = :genreName" )
    Optional<Genre> findByGenre(@Param("genreName") String genreName);
}
