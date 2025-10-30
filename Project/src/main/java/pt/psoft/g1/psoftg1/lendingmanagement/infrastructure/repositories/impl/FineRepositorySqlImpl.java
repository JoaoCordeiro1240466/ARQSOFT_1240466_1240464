package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.FineSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaFineRepository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.FineJpaEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class FineRepositorySqlImpl implements FineRepository {

    private final SpringDataJpaFineRepository jpaRepo;
    private final FineSqlMapper mapper;

    private static final String CACHE_NAME = "fineCache";

    @Override
    @Cacheable(value = CACHE_NAME, key = "#lendingNumber")
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        return jpaRepo.findByLending_LendingNumber_LendingNumber(lendingNumber)
                .map(mapper::toDomain);
    }

    @Override
    public Iterable<Fine> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true) // Evicta o cache ao salvar
    public Fine save(Fine fine) {
        FineJpaEntity entity = mapper.toEntity(fine);
        FineJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }
}