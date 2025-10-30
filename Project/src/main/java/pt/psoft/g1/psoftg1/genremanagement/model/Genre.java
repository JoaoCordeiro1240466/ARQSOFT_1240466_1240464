package pt.psoft.g1.psoftg1.genremanagement.model;

// Imports de persistência REMOVIDOS
// import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

// @Entity e @Table REMOVIDOS
public class Genre {

    // @Transient REMOVIDO
    private final int GENRE_MAX_LENGTH = 100;

    // @Id e @GeneratedValue REMOVIDOS
    @Getter
    private long pk;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    // @Column REMOVIDO
    @Getter
    String genre;

    // Alterado para 'public' para o Mapper
    public Genre(){}

    public Genre(String genre) {
        setGenre(genre);
    }

    private void setGenre(String genre) {
        if(genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if(genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        // Corrigido: a mensagem de erro dizia 4096
        if(genre.length() > GENRE_MAX_LENGTH)
            throw new IllegalArgumentException("Genre has a maximum of " + GENRE_MAX_LENGTH + " characters");
        this.genre = genre;
    }

    public String toString() {
        return genre;
    }

    // --- Setters para o Mapper ---
    // Adicionado para que o Mapper consiga construir o objeto a partir da BD
    public void setPk(long pk) {
        this.pk = pk;
    }
}