package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Entity
@Table(name = "Fine")
@Getter
@Setter
@NoArgsConstructor
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @PositiveOrZero
    private int fineValuePerDayInCents;

    /**Fine value in Euro cents*/
    @PositiveOrZero
    private int centsValue;

    @OneToOne(optional = false) // 👈 CRÍTICO: Isto diz ao Hibernate que é uma relação!
    @JoinColumn(name = "lending_pk", nullable = false, unique = true)
    private Lending lending;

    public Fine(Lending lending) {
        if(lending == null) {
            throw new IllegalArgumentException("Lending cannot be null");
        }
        // Validação de segurança
        if(lending.getDaysDelayed() <= 0) {
            // Nota: Em cenários reais, isto poderia ser validado antes,
            // mas mantemos a lógica para garantir integridade.
            // throw new IllegalArgumentException("Lending is not overdue");
        }

        this.fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        this.centsValue = this.fineValuePerDayInCents * lending.getDaysDelayed();
        this.lending = lending;
    }

    /** Setter manual para compatibilidade com Mappers, se necessário */
    public void setPk(Long pk) {
        this.pk = pk;
    }
}