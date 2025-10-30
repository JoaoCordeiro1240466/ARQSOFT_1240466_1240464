package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.AuthorSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaAuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class AuthorRepositorySqlImpl implements AuthorRepository {

    private final SpringDataJpaAuthorRepository jpaRepo;
    private final AuthorSqlMapper mapper;

    private static final String CACHE_NAME = "authorCache";

    @Override
    @Cacheable(value = CACHE_NAME, key = "#authorNumber")
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        return jpaRepo.findById(authorNumber).map(mapper::toDomain);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return jpaRepo.findByName_NameStartsWith(name)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return jpaRepo.findByName_Name(name)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#author.authorNumber")
    public Author save(Author author) {
        AuthorJpaEntity entity = mapper.toEntity(author);
        AuthorJpaEntity savedEntity = jpaRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Iterable<Author> findAll() {
        return jpaRepo.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#author.authorNumber")
    public void delete(Author author) {
        AuthorJpaEntity entity = mapper.toEntity(author);
        jpaRepo.delete(entity);
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {

        Page<Object[]> rawPage = jpaRepo.findTopAuthorByLendingsRaw(pageableRules);

        return rawPage.map(row -> {
            AuthorJpaEntity authorEntity = (AuthorJpaEntity) row[0];
            Long count = (Long) row[1];
            Author authorDomain = mapper.toDomain(authorEntity);

            // --- ESTA É A LINHA CORRIGIDA ---
            // ANTES: return new AuthorLendingView(authorDomain, count);
            // DEPOIS: Passamos o nome (String) em vez do objeto (Author)
            return new AuthorLendingView(authorDomain.getName(), count);
        });
    }

    // --- MÉTODO CORRIGIDO ---
    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        // Agora podemos chamar a query diretamente e mapear os resultados
        return jpaRepo.findCoAuthorsByAuthorNumber(authorNumber)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}