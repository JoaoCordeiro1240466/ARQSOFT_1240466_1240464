package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper.LendingSqlMapper;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.LendingJpaEntity;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaLendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LendingRepositorySqlImplTest {

    @Mock
    private SpringDataJpaLendingRepository jpaRepo;

    @Mock
    private LendingSqlMapper mapper;

    @InjectMocks
    private LendingRepositorySqlImpl lendingRepository;

    @Test
    void shouldReturnLendingWhenFoundByNumber() {
        // Arrange
        String lendingNumber = "LEND-001";
        LendingJpaEntity entity = new LendingJpaEntity();
        Lending expectedLending = new Lending();

        when(jpaRepo.findByLendingNumber_LendingNumber(lendingNumber)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(expectedLending);

        // Act
        Optional<Lending> result = lendingRepository.findByLendingNumber(lendingNumber);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(expectedLending);
        verify(jpaRepo).findByLendingNumber_LendingNumber(lendingNumber);
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByNumber() {
        // Arrange
        String lendingNumber = "LEND-999";
        when(jpaRepo.findByLendingNumber_LendingNumber(lendingNumber)).thenReturn(Optional.empty());

        // Act
        Optional<Lending> result = lendingRepository.findByLendingNumber(lendingNumber);

        // Assert
        assertThat(result).isEmpty();
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void shouldGetCountFromCurrentYear() {
        // Arrange
        // Capturamos o ano atual *exatamente como a implementação fará*
        int currentYear = LocalDate.now().getYear();
        int expectedCount = 125;

        // Mock: Esperamos uma chamada com o ano atual
        when(jpaRepo.getCountInYear(currentYear)).thenReturn(expectedCount);

        // Act
        int result = lendingRepository.getCountFromCurrentYear();

        // Assert
        assertThat(result).isEqualTo(expectedCount);
        // Verifica se o jpaRepo foi chamado com o ano correto
        verify(jpaRepo).getCountInYear(currentYear);
    }

    @Test
    void shouldGetOverdueWithCorrectPagination() {
        // Arrange
        // 1. Input: Pedimos página 3, com 10 itens
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(3, 10);

        // 2. Expectativa: Spring Data usa 0-indexado (3 - 1 = 2)
        Pageable expectedPageable = PageRequest.of(2, 10);

        // 3. Mocks: Simular o retorno
        LendingJpaEntity entity = new LendingJpaEntity();
        Lending domainLending = new Lending();
        Page<LendingJpaEntity> mockPage = new PageImpl<>(List.of(entity), expectedPageable, 1);

        when(jpaRepo.getOverdue(expectedPageable)).thenReturn(mockPage);
        when(mapper.toDomain(entity)).thenReturn(domainLending);

        // Act
        List<Lending> results = lendingRepository.getOverdue(page);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results).contains(domainLending);

        // Verifica se o JpaRepo foi chamado com o Pageable *correto*
        verify(jpaRepo).getOverdue(expectedPageable);
    }

    @Test
    void shouldSaveAndReturnMappedLending() {
        // Arrange
        Lending lendingToSave = new Lending();
        LendingJpaEntity entityToSave = new LendingJpaEntity();
        LendingJpaEntity savedEntity = new LendingJpaEntity();
        Lending expectedSavedLending = new Lending();

        when(mapper.toEntity(lendingToSave)).thenReturn(entityToSave);
        when(jpaRepo.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedSavedLending);

        // Act
        Lending result = lendingRepository.save(lendingToSave);

        // Assert
        assertThat(result).isSameAs(expectedSavedLending);
        verify(mapper).toEntity(lendingToSave);
        verify(jpaRepo).save(entityToSave);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void shouldSearchLendingsWithCorrectSpecAndPagination() {
        // Arrange
        // 1. Input: Pedimos página 2, com 5 itens
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(2, 5);

        // 2. Expectativa: Spring Data (2 - 1 = 1)
        Pageable expectedPageable = PageRequest.of(1, 5);

        // 3. Mocks: Simular o retorno
        LendingJpaEntity entity = new LendingJpaEntity();
        Lending domainLending = new Lending();
        Page<LendingJpaEntity> mockPage = new PageImpl<>(List.of(entity), expectedPageable, 1);

        // Configura o Mock para aceitar QUALQUER Specification, mas APENAS o Pageable esperado
        when(jpaRepo.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(mockPage);
        when(mapper.toDomain(entity)).thenReturn(domainLending);

        // ArgumentCaptor para verificar a Specification (opcional, mas bom)
        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);

        // Act: Chamamos com alguns filtros de exemplo
        List<Lending> results = lendingRepository.searchLendings(page, "R-123", "ISBN-456", false, null, null);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results).contains(domainLending);

        // Verifica se o jpaRepo foi chamado com os argumentos corretos.
        // Isto prova que a lógica de paginação (page.getNumber() - 1) está correta
        // e que a lógica de Specification foi ativada.
        verify(jpaRepo).findAll(specCaptor.capture(), eq(expectedPageable));

        // Verificação extra: podemos ver que a Specification não é nula
        assertThat(specCaptor.getValue()).isNotNull();
    }
}