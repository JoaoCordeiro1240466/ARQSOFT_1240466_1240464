package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

// Imports das classes do seu projeto (ajuste os pacotes se necessário)
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.AuthorSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.AuthorJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaAuthorRepository;

import java.util.List;
import java.util.Optional;

// Imports estáticos para Mocks e Asserts (facilitam a leitura)
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Unidade para AuthorRepositorySqlImpl.
 * Foco: Verificar se esta classe (o "sujeito") chama corretamente os seus
 * colaboradores (mocks) e se a lógica de mapeamento está correta.
 * Não acede à base de dados.
 */
@ExtendWith(MockitoExtension.class) // Habilita o Mockito para gerir os mocks
class AuthorRepositorySqlImplTest {

    // A(s) dependência(s) que queremos simular (Mocks)
    @Mock
    private SpringDataJpaAuthorRepository jpaRepo;

    @Mock
    private AuthorSqlMapper mapper;

    // A classe que queremos testar. O Mockito vai injetar os mocks acima nela.
    @InjectMocks
    private AuthorRepositorySqlImpl authorRepository;

    // --- Testes para findByAuthorNumber ---

    @Test
    void shouldReturnAuthorWhenFoundByAuthorNumber() {
        // Arrange (Preparar)
        Long authorNumber = 1L;
        AuthorJpaEntity foundEntity = new AuthorJpaEntity(); // Simula entidade do JPA
        Author expectedAuthor = new Author(); // Simula o objeto de domínio
        expectedAuthor.setAuthorNumber(authorNumber);

        // Configurar os mocks:
        // 1. Quando o jpaRepo.findById(1L) for chamado, retorna a "foundEntity"
        when(jpaRepo.findById(authorNumber)).thenReturn(Optional.of(foundEntity));
        // 2. Quando o mapper.toDomain(foundEntity) for chamado, retorna o "expectedAuthor"
        when(mapper.toDomain(foundEntity)).thenReturn(expectedAuthor);

        // Act (Executar)
        Optional<Author> result = authorRepository.findByAuthorNumber(authorNumber);

        // Assert (Verificar)
        assertThat(result).isPresent(); // Verifica se o Optional não está vazio
        assertThat(result.get()).isEqualTo(expectedAuthor); // Verifica se o conteúdo é o esperado

        // Verifica se os mocks foram chamados como esperado
        verify(jpaRepo).findById(authorNumber);
        verify(mapper).toDomain(foundEntity);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByAuthorNumber() {
        // Arrange
        Long authorNumber = 99L;

        // Configurar o mock:
        // Quando o jpaRepo.findById(99L) for chamado, retorna um Optional vazio
        when(jpaRepo.findById(authorNumber)).thenReturn(Optional.empty());

        // Act
        Optional<Author> result = authorRepository.findByAuthorNumber(authorNumber);

        // Assert
        assertThat(result).isEmpty(); // Verifica se o Optional está vazio

        // Verifica se o jpaRepo foi chamado
        verify(jpaRepo).findById(authorNumber);
        // *Importante*: Verifica se o mapper NUNCA foi chamado
        verify(mapper, never()).toDomain(any(AuthorJpaEntity.class));
    }

    // --- Teste para save ---

    @Test
    void shouldSaveAndReturnMappedAuthor() {
        // Arrange
        Author authorToSave = new Author(); // Objeto de domínio (entrada)
        authorToSave.setName("Autor Novo");

        AuthorJpaEntity entityToSave = new AuthorJpaEntity(); // Entidade (intermédio)
        AuthorJpaEntity savedEntity = new AuthorJpaEntity(); // Entidade (retorno do JPA)
        Author expectedSavedAuthor = new Author(); // Objeto de domínio (saída)
        expectedSavedAuthor.setName("Autor Novo");

        // Configurar mocks:
        when(mapper.toEntity(authorToSave)).thenReturn(entityToSave);
        when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedSavedAuthor);

        // Act
        Author result = authorRepository.save(authorToSave);

        // Assert
        assertThat(result).isEqualTo(expectedSavedAuthor);

        // Verifica a sequência de chamadas
        verify(mapper).toEntity(authorToSave);
        verify(jpaRepo).save(entityToSave);
        verify(mapper).toDomain(savedEntity);
    }

    // --- Teste para findTopAuthorByLendings (O mais importante) ---

    @Test
    void shouldCorrectlyMapTopAuthorsFromRawPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5); // O input

        // 1. Criar os dados "crus" (raw) que o JPA retornaria
        AuthorJpaEntity entity1 = new AuthorJpaEntity();
        AuthorJpaEntity entity2 = new AuthorJpaEntity();
        Object[] rawRow1 = new Object[]{entity1, 10L}; // Entidade 1 com 10 empréstimos
        Object[] rawRow2 = new Object[]{entity2, 5L};  // Entidade 2 com 5 empréstimos

        // 2. Criar a Página "falsa" que o jpaRepo vai retornar
        List<Object[]> rawList = List.of(rawRow1, rawRow2);
        Page<Object[]> mockPage = new PageImpl<>(rawList, pageable, rawList.size());

        // 3. Criar os dados de "domínio" que o mapper vai retornar
        Author domain1 = new Author();
        domain1.setName("Autor Teste 1"); // O nome que queremos verificar
        Author domain2 = new Author();
        domain2.setName("Autor Teste 2");

        // 4. Configurar Mocks:
        // O repositório retorna a página "crua"
        when(jpaRepo.findTopAuthorByLendingsRaw(pageable)).thenReturn(mockPage);
        // O mapper converte as entidades
        when(mapper.toDomain(entity1)).thenReturn(domain1);
        when(mapper.toDomain(entity2)).thenReturn(domain2);

        // Act
        Page<AuthorLendingView> resultPage = authorRepository.findTopAuthorByLendings(pageable);

        // Assert
        // Verifica se a página geral está correta
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).hasSize(2);

        // Verifica o mapeamento do primeiro item
        AuthorLendingView view1 = resultPage.getContent().get(0);
        assertThat(view1.getAuthorName()).isEqualTo("Autor Teste 1"); // Verifica se usou o .getName()
        assertThat(view1.getLendingCount()).isEqualTo(10L); // Verifica se usou a contagem

        // Verifica o mapeamento do segundo item
        AuthorLendingView view2 = resultPage.getContent().get(1);
        assertThat(view2.getAuthorName()).isEqualTo("Autor Teste 2");
        assertThat(view2.getLendingCount()).isEqualTo(5L);

        // Verifica as interações
        verify(jpaRepo).findTopAuthorByLendingsRaw(pageable);
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }
}