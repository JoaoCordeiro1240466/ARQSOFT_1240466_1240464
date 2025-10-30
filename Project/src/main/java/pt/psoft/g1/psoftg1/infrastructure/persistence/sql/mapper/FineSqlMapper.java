package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.FineJpaEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

@Component
@RequiredArgsConstructor
public class FineSqlMapper {

    // Depende do LendingMapper para traduzir a relação
    private final LendingSqlMapper lendingMapper;

    /**
     * Converte a Entidade (JPA) para o Domínio.
     */
    public Fine toDomain(FineJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Fine domain = new Fine();
        domain.setPk(entity.getPk());
        domain.setCentsValue(entity.getCentsValue());
        domain.setFineValuePerDayInCents(entity.getFineValuePerDayInCents());

        // Evita recursão infinita ao mapear a relação
        if (entity.getLending() != null) {
            domain.setLending(lendingMapper.toDomain(entity.getLending()));
        }

        return domain;
    }

    /**
     * Converte o Domínio para a Entidade (JPA).
     */
    public FineJpaEntity toEntity(Fine domain) {
        if (domain == null) {
            return null;
        }

        FineJpaEntity entity = new FineJpaEntity();
        entity.setPk(domain.getPk());
        entity.setCentsValue(domain.getCentsValue());
        entity.setFineValuePerDayInCents(domain.getFineValuePerDayInCents());

        // Evita recursão infinita ao mapear a relação
        if (domain.getLending() != null) {
            entity.setLending(lendingMapper.toEntity(domain.getLending()));
        }

        return entity;
    }
}