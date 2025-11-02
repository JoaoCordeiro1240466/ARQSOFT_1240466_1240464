package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_GENRE", uniqueConstraints = {
        @UniqueConstraint(name = "uc_genre_name", columnNames = {"genre"})
})
@Getter
@Setter
@NoArgsConstructor
public class GenreJpaEntity {

    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pk;

    @Size(min = 1, max = GENRE_MAX_LENGTH)
    @Column(unique = true, nullable = false, length = GENRE_MAX_LENGTH)
    private String genre;

    public GenreJpaEntity(String genre) {
        this.genre = genre;
    }
}