package pt.psoft.g1.psoftg1.genremanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 👈 OBRIGATÓRIO para ser uma tabela
@Table(name = "Genre")
@NoArgsConstructor
public class Genre {

    @Transient // 👈 Diz ao JPA para ignorar este campo (não cria coluna)
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 👈 Gera o ID automaticamente
    @Getter
    @Setter
    private Long pk; // Mudei para Long (objeto) que é melhor prática em JPA, mas long primitivo também dá

    @Version
    @Getter
    private Long version;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(nullable = false, unique = true) // 👈 Garante que o nome é guardado e único
    @Getter
    private String genre;

    public Genre(String genre) {
        setGenre(genre);
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