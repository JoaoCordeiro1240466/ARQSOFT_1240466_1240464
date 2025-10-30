package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

// Imports do Lombok e Spring (sem alteração)
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

// Imports da tua camada de Domínio/Aplicação (sem alteração)
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

// Imports das tuas novas classes de INFRAESTRUTURA
// Repara como estes imports apontam para as pastas que criámos antes:
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.UserSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaUserRepository;

// Imports do Java (sem alteração)
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação SQL (H2) do repositório de User.
 *
 * Esta classe é ativada apenas quando o perfil "sql" está ativo.
 *
 * Ela implementa o 'UserRepository' (o contrato do domínio).
 * Ela USA o 'SpringDataJpaUserRepository' (o trabalhador do Spring Data).
 * Ela USA o 'UserSqlMapper' (o tradutor).
 *
 * Também gere a lógica de cache (Redis).
 */
@Repository
@Profile("sql") // Diz ao Spring para só criar este bean se o perfil "sql" estiver ativo
@RequiredArgsConstructor // Cria um construtor com os campos 'final'
public class UserRepositorySqlImpl implements UserRepository {

    // Nossas dependências de infraestrutura (injetadas pelo Spring)
    private final SpringDataJpaUserRepository jpaRepo; // O trabalhador
    private final UserSqlMapper mapper; // O tradutor

    // Define o nome do cache que o Redis vai usar
    private static final String CACHE_NAME = "userCache";

    /**
     * Tenta encontrar o User no cache 'userCache' com a chave '#id'.
     * Se não encontrar, executa o método e coloca o resultado no cache.
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "#id")
    public Optional<User> findById(String id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    /**
     * Tenta encontrar o User no cache 'userCache' com a chave '#username'.
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "#username")
    public Optional<User> findByUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
    }

    /**
     * Remove (expulsa) um User do cache 'userCache' sempre que é guardado.
     * A chave é o 'id' do User que está a ser guardado.
     * Isto garante que o cache não fica com dados desatualizados.
     */
    @Override
    @CacheEvict(value = CACHE_NAME, key = "#entity.id")
    public <S extends User> S save(S entity) {
        UserJpaEntity entityToSave = mapper.toEntity(entity);
        UserJpaEntity savedEntity = jpaRepo.save(entityToSave);
        return (S) mapper.toDomain(savedEntity);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true) // Limpa o cache todo ao salvar muitos
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<UserJpaEntity> entitiesToSave =
                ((List<S>) entities).stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList());

        List<UserJpaEntity> savedEntities = jpaRepo.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(mapper::toDomain)
                .map(user -> (S) user)
                .collect(Collectors.toList());
    }

    /**
     * Remove o User do cache quando é apagado.
     */
    @Override
    @CacheEvict(value = CACHE_NAME, key = "#user.id")
    public void delete(User user) {
        UserJpaEntity entityToDelete = mapper.toEntity(user);
        jpaRepo.delete(entityToDelete);
    }

    @Override
    public List<User> findByNameName(String name) {
        return jpaRepo.findByName_Name(name)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        return jpaRepo.findByName_NameContains(name)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {


        Specification<UserJpaEntity> spec = (root, q, cb) -> cb.conjunction(); // Spec. "vazia"

        PageRequest pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());

        org.springframework.data.domain.Page<UserJpaEntity> jpaPage = jpaRepo.findAll(spec, pageable);

        return jpaPage.getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}