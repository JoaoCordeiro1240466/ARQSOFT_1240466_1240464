package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.FineSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.FineJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaFineRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FineRepositorySqlImplTest {

    @Mock
    private SpringDataJpaFineRepository jpaRepo;

    @Mock
    private FineSqlMapper mapper;

    @InjectMocks
    private FineRepositorySqlImpl fineRepository;

    @Test
    void shouldReturnFineWhenFoundByLendingNumber() {
        // Arrange
        String lendingNumber = "LEND-123";
        FineJpaEntity foundEntity = new FineJpaEntity(); // Entidade da BD
        Fine expectedFine = new Fine(); // Objeto de Domínio

        // Configurar Mocks
        when(jpaRepo.findByLending_LendingNumber_LendingNumber(lendingNumber))
                .thenReturn(Optional.of(foundEntity));
        when(mapper.toDomain(foundEntity)).thenReturn(expectedFine);

        // Act
        Optional<Fine> result = fineRepository.findByLendingNumber(lendingNumber);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(expectedFine); // Verifica se é o mesmo objeto

        // Verifica as chamadas
        verify(jpaRepo).findByLending_LendingNumber_LendingNumber(lendingNumber);
        verify(mapper).toDomain(foundEntity);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByLendingNumber() {
        // Arrange
        String lendingNumber = "LEND-999";

        // Configurar Mock
        when(jpaRepo.findByLending_LendingNumber_LendingNumber(lendingNumber))
                .thenReturn(Optional.empty());

        // Act
        Optional<Fine> result = fineRepository.findByLendingNumber(lendingNumber);

        // Assert
        assertThat(result).isEmpty();

        // Verifica que o mapper NUNCA foi chamado
        verify(jpaRepo).findByLending_LendingNumber_LendingNumber(lendingNumber);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void shouldSaveAndReturnMappedFine() {
        // Arrange
        Fine fineToSave = new Fine(); // Domínio (entrada)
        FineJpaEntity entityToSave = new FineJpaEntity(); // Entidade (intermédio)
        FineJpaEntity savedEntity = new FineJpaEntity(); // Entidade (retorno do JPA)
        Fine expectedSavedFine = new Fine(); // Domínio (saída)

        // Configurar Mocks
        when(mapper.toEntity(fineToSave)).thenReturn(entityToSave);
        when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedSavedFine);

        // Act
        Fine result = fineRepository.save(fineToSave);

        // Assert
        assertThat(result).isSameAs(expectedSavedFine);

        // Verifica se a sequência de chamadas foi correta
        InOrder inOrder = inOrder(mapper, jpaRepo);
        inOrder.verify(mapper).toEntity(fineToSave);    // 1º: Mapeia para entidade
        inOrder.verify(jpaRepo).save(entityToSave);     // 2º: Guarda a entidade
        inOrder.verify(mapper).toDomain(savedEntity);   // 3º: Mapeia de volta para domínio
    }
    @Test
    void shouldFindAllAndMapResults() {
        // Arrange
        FineJpaEntity entity1 = new FineJpaEntity();
        FineJpaEntity entity2 = new FineJpaEntity();
        List<FineJpaEntity> entityList = List.of(entity1, entity2);

        Fine fine1 = new Fine();
        Fine fine2 = new Fine();

        // Configurar Mocks
        when(jpaRepo.findAll()).thenReturn(entityList);
        when(mapper.toDomain(entity1)).thenReturn(fine1);
        when(mapper.toDomain(entity2)).thenReturn(fine2);

        // Act
        Iterable<Fine> result = fineRepository.findAll();

        // Assert
        assertThat(result).containsExactlyInAnyOrder(fine1, fine2);

        // Verifica as chamadas
        verify(jpaRepo).findAll();
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }
}