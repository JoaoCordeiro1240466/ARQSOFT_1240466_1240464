package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @OneToOne
    private UserJpaEntity reader;

    @Embedded
    private ReaderNumber readerNumber;

    @Embedded
    private BirthDate birthDate;

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

    @ManyToMany

    @JoinTable(
            name = "T_READER_INTERESTS",
            joinColumns = @JoinColumn(name = "reader_pk"),
            inverseJoinColumns = @JoinColumn(name = "genre_pk")
    )
    private List<GenreJpaEntity> interestList;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "photo_id", nullable = true)
    private Photo photo;
}