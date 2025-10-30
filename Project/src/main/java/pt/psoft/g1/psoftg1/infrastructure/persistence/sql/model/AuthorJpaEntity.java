package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_AUTHOR")
@Getter
@Setter
@NoArgsConstructor
public class AuthorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUTHOR_NUMBER")
    private Long authorNumber;

    @Version
    private long version;

    @Embedded
    private Name name;

    @Embedded
    private Bio bio;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "photo_id", nullable = true)
    private Photo photo;


    /**
     * Relação inversa para Book (necessária para a query 'findCoAuthors')
     * "mappedBy = authors" diz que a entidade BookJpaEntity (o campo 'authors')
     * é a "dona" desta relação.
     */
    @ManyToMany(mappedBy = "authors")
    private List<BookJpaEntity> books = new ArrayList<>();
}