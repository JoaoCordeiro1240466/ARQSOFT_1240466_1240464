package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.ReaderDetailsSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.ReaderDetailsJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class ReaderRepositorySqlImpl implements ReaderRepository {

    private final SpringDataJpaReaderRepository jpaRepo;
    private final ReaderDetailsSqlMapper mapper;

    private static final String CACHE_NAME = "readerCache";

    @Override
    @Cacheable(value = CACHE_NAME, key = "#readerNumber")
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return jpaRepo.findByReaderNumber_ReaderNumber(readerNumber).map(mapper::toDomain);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return jpaRepo.findByPhoneNumber_PhoneNumber(phoneNumber).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#username")
    public Optional<ReaderDetails> findByUsername(String username) {
        return jpaRepo.findByReader_Username(username).map(mapper::toDomain);
    }

    /**
     * Corrigido: A interface pedia um Long, mas o ID do User é String.
     * Convertemos o Long para String para chamar o repo interno.
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "#userId")
    public Optional<ReaderDetails> findByUserId(Long userId) {
        return jpaRepo.findByReader_Id(userId.toString()).map(mapper::toDomain);
    }

    @Override
    public int getCountFromCurrentYear() {
        return jpaRepo.getCountInYear(LocalDate.now().getYear());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true) // Invalida cache ao salvar
    public ReaderDetails save(ReaderDetails readerDetails) {
        ReaderDetailsJpaEntity entity = mapper.toEntity(readerDetails);
        ReaderDetailsJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        // Usa o método findAll(Pageable) do JpaRepository
        return jpaRepo.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void delete(ReaderDetails readerDetails) {
        jpaRepo.delete(mapper.toEntity(readerDetails));
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        // TODO: Implementar Specification dinâmica baseada no SearchReadersQuery
        Specification<ReaderDetailsJpaEntity> spec = (root, q, cb) -> cb.conjunction();
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page.getNumber() - 1, page.getLimit());

        return jpaRepo.findAll(spec, pageable).getContent().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    // --- MÉTODOS COMPLEXOS (TEMPORARIAMENTE DESLIGADOS) ---

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        // TODO: Descomentar quando LendingJpaEntity estiver criada
        // return jpaRepo.findTopByGenre(pageable, genre, startDate, endDate);

        // Retorno temporário para compilar
        return Page.empty(pageable);
    }
}