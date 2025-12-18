package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Entity // 👈 OBRIGATÓRIO: Define que isto é uma tabela
@Table(name = "Lending")
@Getter
@Setter
@EqualsAndHashCode(of = "lendingNumber")
@NoArgsConstructor // O JPA exige um construtor vazio
public class Lending {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Column(nullable = false, unique = true) // 👈 O número de empréstimo deve ser único na BD
    private String lendingNumber;

    @NotNull
    @Column(nullable = false)
    private String isbn;

    @NotNull
    @Column(nullable = false)
    private String readerId;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate limitDate;

    private LocalDate returnedDate;

    @Version // 👈 Para controlo de concorrência (Optimistic Locking)
    private long version;

    @Size(min = 0, max = 1024)
    @Setter
    @Column(length = 1024)
    private String commentary = null;

    // 👇 Estes campos são calculados, não devem ser colunas na BD
    @Transient
    private Integer daysUntilReturn;

    @Transient
    private Integer daysOverdue;

    @Column(nullable = false)
    private int fineValuePerDayInCents;

    // Construtor principal
    public Lending(String isbn, String readerId, String lendingNumber, int lendingDuration, int fineValuePerDayInCents){
        try {
            this.isbn = Objects.requireNonNull(isbn);
            this.readerId = Objects.requireNonNull(readerId);
            this.lendingNumber = Objects.requireNonNull(lendingNumber);
        } catch (NullPointerException e){
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
    }

    public void setReturned(final long desiredVersion, final String commentary){
        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // Nota: O JPA trata da verificação da versão automaticamente se usares @Version,
        // mas podes manter esta verificação manual se quiseres lançar a tua exceção específica.
        if (this.version != desiredVersion)
            throw new ConflictException("Object was already modified by another user");

        if(commentary != null)
            this.commentary = commentary;

        this.returnedDate = LocalDate.now();
    }

    public int getDaysDelayed(){
        if(this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        } else {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    private void setDaysUntilReturn(){
        int days = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        if(this.returnedDate != null || days < 0){
            this.daysUntilReturn = null;
        } else {
            this.daysUntilReturn = days;
        }
    }

    private void setDaysOverdue(){
        int days = getDaysDelayed();
        if(days > 0){
            this.daysOverdue = days;
        } else {
            this.daysOverdue = null;
        }
    }

    public Optional<Integer> getDaysUntilReturn() {
        setDaysUntilReturn();
        return Optional.ofNullable(daysUntilReturn);
    }

    public Optional<Integer> getDaysOverdue() {
        setDaysOverdue();
        return Optional.ofNullable(daysOverdue);
    }

    public Optional<Integer> getFineValueInCents() {
        Optional<Integer> fineValueInCents = Optional.empty();
        int days = getDaysDelayed();
        if(days > 0){
            fineValueInCents = Optional.of(fineValuePerDayInCents * days);
        }
        return fineValueInCents;
    }

    // Factory method para bootstrapping (se necessário)
    public static Lending newBootstrappingLending(String isbn,
                                                  String readerId,
                                                  String lendingNumber,
                                                  LocalDate startDate,
                                                  LocalDate returnedDate,
                                                  int lendingDuration,
                                                  int fineValuePerDayInCents){
        Lending lending = new Lending();
        lending.isbn = Objects.requireNonNull(isbn);
        lending.readerId = Objects.requireNonNull(readerId);
        lending.lendingNumber = Objects.requireNonNull(lendingNumber);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;
    }

    // Getters manuais ou setters extra se necessário (o Lombok @Getter já trata da maioria)
    // Mantive o getReaderId() por compatibilidade com o Mapper se ele o usar
    public String getReaderCode() {
        return this.readerId;
    }
}