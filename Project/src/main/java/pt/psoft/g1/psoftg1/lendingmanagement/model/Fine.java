package pt.psoft.g1.psoftg1.lendingmanagement.model;

// Imports JPA REMOVIDOS
// import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
// @Entity REMOVIDO
public class Fine {

    // @Id, @GeneratedValue REMOVIDOS
    private Long pk;

    @PositiveOrZero
    // @Column REMOVIDO
    @Setter // Adicionado para o Mapper
    private int fineValuePerDayInCents;

    /**Fine value in Euro cents*/
    @PositiveOrZero
    @Setter // Adicionado para o Mapper
            int centsValue;

    @Setter
    // @OneToOne, @JoinColumn REMOVIDOS
    private Lending lending;

    public Fine(Lending lending) {
        if(lending.getDaysDelayed() <= 0)
            throw new IllegalArgumentException("Lending is not overdue");
        fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        centsValue = fineValuePerDayInCents * lending.getDaysDelayed();
        this.lending = Objects.requireNonNull(lending);
    }

    /** Alterado para 'public' para o Mapper */
    public Fine() {}

    /** Adicionado para o Mapper */
    public void setPk(Long pk) {
        this.pk = pk;
    }
}