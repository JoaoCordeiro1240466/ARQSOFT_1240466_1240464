package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.UserSqlMapper;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.UserSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.AuthorSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.BookSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.GenreSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.ReaderDetailsSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.LendingSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.FineSqlMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.UserRepositorySqlImpl;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.AuthorRepositorySqlImpl;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.BookRepositorySqlImpl;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.GenreRepositorySqlImpl;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.ReaderRepositorySqlImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.LendingRepositorySqlImpl;
import pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl.FineRepositorySqlImpl;

@DataJpaTest
@ActiveProfiles("sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        // Os 7 Mappers
        UserSqlMapper.class,
        AuthorSqlMapper.class,
        BookSqlMapper.class,
        GenreSqlMapper.class,
        ReaderDetailsSqlMapper.class,
        LendingSqlMapper.class,
        FineSqlMapper.class,

        // As 7 Implementações
        UserRepositorySqlImpl.class,
        AuthorRepositorySqlImpl.class,
        BookRepositorySqlImpl.class,
        GenreRepositorySqlImpl.class,
        ReaderRepositorySqlImpl.class,
        LendingRepositorySqlImpl.class,
        FineRepositorySqlImpl.class
})
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository; // Este é o nosso repositório (interface de domínio)

    @Test
    public void whenFindByName_thenReturnAuthor() {

        // given
        // 1. Temos de criar a ENTIDADE JPA (AuthorJpaEntity), não o modelo de domínio
        AuthorJpaEntity alexEntity = new AuthorJpaEntity();
        alexEntity.setName(new Name("Alex"));
        alexEntity.setBio(new Bio("O Alex escreveu livros"));
        // (O ID/AuthorNumber será gerado automaticamente pelo @GeneratedValue)

        // 2. Persistimos a ENTIDADE JPA
        entityManager.persist(alexEntity);
        entityManager.flush();

        // when
        // 3. Usamos o nosso repositório, que (corretamente) devolve o Modelo de Domínio
        List<Author> list = authorRepository.searchByNameName("Alex");

        // then
        // 4. Verificamos o Modelo de Domínio
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName()).isEqualTo("Alex");
    }
}