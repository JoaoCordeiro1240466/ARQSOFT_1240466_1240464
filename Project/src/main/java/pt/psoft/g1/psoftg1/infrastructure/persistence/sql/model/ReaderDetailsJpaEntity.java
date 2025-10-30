package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Importa a ENTIDADE JPA, não o modelo de domínio
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.GenreJpaEntity;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.List;

@Entity
@Table(name = "READER_DETAILS")
@Getter
@Setter
@NoArgsConstructor
public class ReaderDetailsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    /**
     * Relação com a UserJpaEntity (a entidade que representa o 'Reader')
     */
    @OneToOne
    private UserJpaEntity reader; // <-- Aponta para a Entidade JPA

    /**
     * Assumindo que ReaderNumber é @Embeddable
     */
    @Embedded
    private ReaderNumber readerNumber;

    @Embedded
    private BirthDate birthDate; // Já é @Embeddable

    /**
     * Assumindo que PhoneNumber é @Embeddable
     */
    @Embedded
    private PhoneNumber phoneNumber;

    @Basic
    private boolean gdprConsent;

    @Basic
    private boolean marketingConsent;

    @Basic
    private boolean thirdPartySharingConsent;

    @Version
    private Long version;

    /**
     * Relação com GenreJpaEntity
     */
    @ManyToMany

    @JoinTable(
            name = "T_READER_INTERESTS", // Define o nome da tabela de junção
            joinColumns = @JoinColumn(name = "reader_pk"), // Chave desta entidade
            inverseJoinColumns = @JoinColumn(name = "genre_pk") // Chave da outra entidade
    )
    private List<GenreJpaEntity> interestList; // <-- Aponta para a Entidade JPA

    /**
     * Relação com Photo (vinda da EntityWithPhoto)
     */
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "photo_id", nullable = true)
    private Photo photo;
}