package pt.psoft.g1.psoftg1.genremanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Genre")
@NoArgsConstructor
public class Genre {

    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @Getter
    private String genreId;

    @Version
    @Getter
    private Long version;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(nullable = false, unique = true)
    @Getter
    private String genre;

    public Genre(String genreId, String genre) {
        setGenreId(genreId);
        setGenre(genre);
    }

    public Genre(String genre) {
        setGenre(genre);
    }

    private void setGenreId(String genreId) {
        if (genreId == null || genreId.isBlank()) {
            throw new IllegalArgumentException("Genre ID cannot be null or blank");
        }
        this.genreId = genreId;
    }

    private void setGenre(String genre) {
        if(genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if(genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        if(genre.length() > GENRE_MAX_LENGTH)
            throw new IllegalArgumentException("Genre has a maximum of " + GENRE_MAX_LENGTH + " characters");
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }
}
