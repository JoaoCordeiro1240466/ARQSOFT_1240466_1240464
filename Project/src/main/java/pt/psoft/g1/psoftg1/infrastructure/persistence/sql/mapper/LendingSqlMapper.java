package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.LendingJpaEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

/**
 * Mapper (Tradutor) entre o modelo de domínio Lending e a entidade JPA LendingJpaEntity.
 */
@Component
@RequiredArgsConstructor
public class LendingSqlMapper {

    // Dependência dos mappers das entidades relacionadas
    private final BookSqlMapper bookMapper;
    private final ReaderDetailsSqlMapper readerDetailsMapper;

    /**
     * Converte a Entidade (JPA) para o Domínio.
     * (Usado quando LÊS da base de dados).
     */
    public Lending toDomain(LendingJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Lending domain = new Lending();

        // Preenche os campos de ID e Versão
        domain.setPk(entity.getPk());
        domain.setVersion(entity.getVersion());

        // Preenche os campos simples
        domain.setLendingNumber(entity.getLendingNumber());
        domain.setStartDate(entity.getStartDate());
        domain.setLimitDate(entity.getLimitDate());
        domain.setReturnedDate(entity.getReturnedDate());
        domain.setCommentary(entity.getCommentary());
        domain.setFineValuePerDayInCents(entity.getFineValuePerDayInCents());

        // Preenche as Relações usando os outros mappers
        if (entity.getBook() != null) {
            domain.setBook(bookMapper.toDomain(entity.getBook()));
        }

        if (entity.getReaderDetails() != null) {
            domain.setReaderDetails(readerDetailsMapper.toDomain(entity.getReaderDetails()));
        }

        return domain;
    }

    /**
     * Converte o Domínio para a Entidade (JPA).
     * (Usado quando QUERES GUARDAR na base de dados).
     */
    public LendingJpaEntity toEntity(Lending domain) {
        if (domain == null) {
            return null;
        }

        LendingJpaEntity entity = new LendingJpaEntity();

        // Preenche os campos de ID e Versão
        entity.setPk(domain.getPk());
        entity.setVersion(domain.getVersion());

        // Preenche os campos simples
        entity.setLendingNumber(domain.getLendingNumberObject());
        entity.setStartDate(domain.getStartDate());
        entity.setLimitDate(domain.getLimitDate());
        entity.setReturnedDate(domain.getReturnedDate());
        entity.setCommentary(domain.getCommentary());
        entity.setFineValuePerDayInCents(domain.getFineValuePerDayInCents());

        // Preenche as Relações usando os outros mappers
        if (domain.getBook() != null) {
            entity.setBook(bookMapper.toEntity(domain.getBook()));
        }

        if (domain.getReaderDetails() != null) {
            entity.setReaderDetails(readerDetailsMapper.toEntity(domain.getReaderDetails()));
        }

        return entity;
    }
}