package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

// Imports das classes de domínio
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

// Imports das classes de infraestrutura (os "ajudantes")
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.LendingJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaLendingRepository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.LendingSqlMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação SQL (H2) do repositório de Lending.
 * Ativado apenas quando o perfil "sql" está ativo.
 */
@Repository
@Profile("sql")
@RequiredArgsConstructor
public class LendingRepositorySqlImpl implements LendingRepository {

    private final SpringDataJpaLendingRepository jpaRepo;
    private final LendingSqlMapper mapper;

    private static final String CACHE_NAME = "lendingCache";

    @Override
    @Cacheable(value = CACHE_NAME, key = "#lendingNumber")
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        return jpaRepo.findByLendingNumber_LendingNumber(lendingNumber).map(mapper::toDomain);
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        return jpaRepo.listByReaderNumberAndIsbn(readerNumber, isbn).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public int getCountFromCurrentYear() {
        return jpaRepo.getCountInYear(LocalDate.now().getYear());
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        return jpaRepo.listOutstandingByReaderNumber(readerNumber).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Double getAverageDuration() {
        return jpaRepo.getAverageDuration();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        return jpaRepo.getAvgLendingDurationByIsbn(isbn);
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        Pageable pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());
        return jpaRepo.getOverdue(pageable).getContent().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true) // Invalida o cache ao salvar
    public Lending save(Lending lending) {
        LendingJpaEntity entity = mapper.toEntity(lending);
        LendingJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true) // Invalida o cache ao apagar
    public void delete(Lending lending) {
        jpaRepo.delete(mapper.toEntity(lending));
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        // Esta é a query dinâmica.
        // Usamos Specification (do JpaSpecificationExecutor) para construir a query
        // com base nos filtros que não são nulos.

        Specification<LendingJpaEntity> spec = Specification.where(null);

        if (readerNumber != null && !readerNumber.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("readerDetails").get("readerNumber").get("readerNumber"), readerNumber)
            );
        }

        if (isbn != null && !isbn.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("book").get("isbn").get("isbn"), isbn)
            );
        }

        if (returned != null) {
            if (returned) {
                spec = spec.and((root, query, cb) -> cb.isNotNull(root.get("returnedDate")));
            } else {
                spec = spec.and((root, query, cb) -> cb.isNull(root.get("returnedDate")));
            }
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("startDate"), startDate)
            );
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("startDate"), endDate)
            );
        }

        Pageable pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());

        return jpaRepo.findAll(spec, pageable).getContent().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}