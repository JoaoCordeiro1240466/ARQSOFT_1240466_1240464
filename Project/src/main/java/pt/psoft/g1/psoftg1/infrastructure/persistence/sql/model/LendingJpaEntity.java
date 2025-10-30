package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;

import java.time.LocalDate;

@Entity
@Table(name = "T_LENDING", uniqueConstraints = { // Alterado para T_LENDING
        @UniqueConstraint(columnNames={"LENDING_NUMBER"})})
@Getter
@Setter
@NoArgsConstructor
public class LendingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    /**
     * Assumindo que LendingNumber é @Embeddable
     */
    @Embedded
    private LendingNumber lendingNumber;

    /**
     * Relação com a entidade Book
     */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private BookJpaEntity book; // <-- Aponta para a Entidade JPA

    /**
     * Relação com a entidade ReaderDetails
     */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ReaderDetailsJpaEntity readerDetails; // <-- Aponta para a Entidade JPA

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate limitDate;

    @Temporal(TemporalType.DATE)
    private LocalDate returnedDate;

    @Version
    private long version;

    @Size(min = 0, max = 1024)
    @Column(length = 1024)
    private String commentary = null;

    private int fineValuePerDayInCents;

    @OneToOne(mappedBy = "lending", cascade = CascadeType.ALL, orphanRemoval = true)
    private FineJpaEntity fine;
}