package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
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

    @ManyToMany

    @JoinTable(
            name = "T_BOOK_AUTHORS",
            joinColumns = @JoinColumn(name = "book_pk"),
            inverseJoinColumns = @JoinColumn(name = "author_number")
    )
    private List<AuthorJpaEntity> authors = new ArrayList<>();

    @Embedded
    private Description description;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "photo_id", nullable = true)
    private Photo photo;
}