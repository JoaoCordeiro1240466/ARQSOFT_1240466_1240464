package pt.psoft.g1.psoftg1.lendingmanagement.model;

// Imports de persistência REMOVIDOS
// import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
// Import do Hibernate REMOVIDO
// import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
// Import da exceção de domínio
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.EqualsAndHashCode;
import java.util.Objects;
import java.util.Optional;

/**
 * Modelo de Domínio "Lending".
 * Limpo de anotações de persistência.
 */
// @Entity e @Table REMOVIDOS
@Getter
@EqualsAndHashCode(of = "lendingNumber")
public class Lending {

    // @Id e @GeneratedValue REMOVIDOS
    @Getter
    private Long pk;

    @Getter
    private LendingNumber lendingNumber;

    @NotNull
    @Getter
    // @ManyToOne REMOVIDO
    private Book book;

    @NotNull
    @Getter
    // @ManyToOne REMOVIDO
    private ReaderDetails readerDetails;

    @NotNull
    // @Column e @Temporal REMOVIDOS
    @Getter
    private LocalDate startDate;

    @NotNull
    // @Column e @Temporal REMOVIDOS
    @Getter
    private LocalDate limitDate;

    // @Temporal REMOVIDO
    @Getter
    private LocalDate returnedDate;

    // @Version REMOVIDO
    @Getter
    private long version;

    @Size(min = 0, max = 1024)
    // @Column REMOVIDO
    @Getter
    private String commentary = null;

    // @Transient REMOVIDO (JPA já não existe)
    private Integer daysUntilReturn;

    // @Transient REMOVIDO
    private Integer daysOverdue;

    @Getter
    private int fineValuePerDayInCents;


    /**
     * Construtor de negócio (mantido)
     */
    public Lending(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents){
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        }catch (NullPointerException e){
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
    }

    /**
     * Alterado para 'public' para o Mapper
     */
    public Lending() {}

    /**
     * Lógica de negócio (mantida), com exceção do Hibernate substituída
     */
    public void setReturned(final long desiredVersion, final String commentary){

        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // check current version
        if (this.version != desiredVersion)
            // Exceção do Hibernate substituída por exceção de domínio
            throw new ConflictException("Object was already modified by another user");

        if(commentary != null)
            this.commentary = commentary;

        this.returnedDate = LocalDate.now();
    }

    // --- O resto da lógica de negócio (mantida 100%) ---

    public int getDaysDelayed(){
        if(this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        }else{
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    private void setDaysUntilReturn(){
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        if(this.returnedDate != null || daysUntilReturn < 0){
            this.daysUntilReturn = null;
        }else{
            this.daysUntilReturn = daysUntilReturn;
        }
    }

    private void setDaysOverdue(){
        int days = getDaysDelayed();
        if(days > 0){
            this.daysOverdue = days;
        }else{
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

    public String getTitle(){
        return this.book.getTitle().toString();
    }

    public String getLendingNumber() {
        // Adicionada verificação de nulo para o Mapper
        return this.lendingNumber != null ? this.lendingNumber.toString() : null;
    }

    /**Factory method mantido.*/
    public static Lending newBootstrappingLending(Book book,
                                                  ReaderDetails readerDetails,
                                                  int year,
                                                  int seq,
                                                  LocalDate startDate,
                                                  LocalDate returnedDate,
                                                  int lendingDuration,
                                                  int fineValuePerDayInCents){
        Lending lending = new Lending();
        lending.book = Objects.requireNonNull(book);
        lending.readerDetails = Objects.requireNonNull(readerDetails);
        lending.lendingNumber = new LendingNumber(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;
    }

    // --- Setters para o Mapper (necessários para re-hidratação) ---

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public void setLendingNumber(LendingNumber lendingNumber) {
        this.lendingNumber = lendingNumber;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setReaderDetails(ReaderDetails readerDetails) {
        this.readerDetails = readerDetails;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public void setFineValuePerDayInCents(int fineValuePerDayInCents) {
        this.fineValuePerDayInCents = fineValuePerDayInCents;
    }

    public LendingNumber getLendingNumberObject() {
        return this.lendingNumber;
    }
}