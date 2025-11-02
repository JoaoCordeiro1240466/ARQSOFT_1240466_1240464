package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.LendingJpaEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Component
@RequiredArgsConstructor
public class LendingSqlMapper {

    private final BookSqlMapper bookMapper;
    private final ReaderDetailsSqlMapper readerDetailsMapper;

    public Lending toDomain(LendingJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Lending domain = new Lending();

        domain.setPk(entity.getPk());
        domain.setVersion(entity.getVersion());

        domain.setLendingNumber(entity.getLendingNumber());
        domain.setStartDate(entity.getStartDate());
        domain.setLimitDate(entity.getLimitDate());
        domain.setReturnedDate(entity.getReturnedDate());
        domain.setCommentary(entity.getCommentary());
        domain.setFineValuePerDayInCents(entity.getFineValuePerDayInCents());

        if (entity.getBook() != null) {
            domain.setBook(bookMapper.toDomain(entity.getBook()));
        }

        if (entity.getReaderDetails() != null) {
            domain.setReaderDetails(readerDetailsMapper.toDomain(entity.getReaderDetails()));
        }

        return domain;
    }

    public LendingJpaEntity toEntity(Lending domain) {
        if (domain == null) {
            return null;
        }

        LendingJpaEntity entity = new LendingJpaEntity();

        entity.setPk(domain.getPk());
        entity.setVersion(domain.getVersion());

        entity.setLendingNumber(domain.getLendingNumberObject());
        entity.setStartDate(domain.getStartDate());
        entity.setLimitDate(domain.getLimitDate());
        entity.setReturnedDate(domain.getReturnedDate());
        entity.setCommentary(domain.getCommentary());
        entity.setFineValuePerDayInCents(domain.getFineValuePerDayInCents());

        if (domain.getBook() != null) {
            entity.setBook(bookMapper.toEntity(domain.getBook()));
        }

        if (domain.getReaderDetails() != null) {
            entity.setReaderDetails(readerDetailsMapper.toEntity(domain.getReaderDetails()));
        }

        return entity;
    }
}