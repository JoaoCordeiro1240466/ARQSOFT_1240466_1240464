package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
// Importa a ENTIDADE JPA, não o modelo de domínio
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_BOOK", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
@Getter
@Setter
@NoArgsConstructor
public class BookJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pk;

    @Version
    private Long version;

    @Embedded
    private Isbn isbn;

    @Embedded
    @NotNull
    private Title title;

    @ManyToOne
    @NotNull
    private GenreJpaEntity genre;

    /**
     * Relação com AuthorJpaEntity.
     * Esta é a "dona" da relação.
     */
    @ManyToMany

    @JoinTable(
            name = "T_BOOK_AUTHORS", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "book_pk"), // Chave desta entidade
            inverseJoinColumns = @JoinColumn(name = "author_number") // Chave da outra entidade
    )
    private List<AuthorJpaEntity> authors = new ArrayList<>(); // <-- TEM DE APONTAR PARA A ENTIDADE JPA

    @Embedded
    private Description description;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "photo_id", nullable = true)
    private Photo photo;
}