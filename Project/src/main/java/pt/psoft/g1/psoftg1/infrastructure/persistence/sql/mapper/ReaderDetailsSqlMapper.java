package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import jakarta.persistence.EntityNotFoundException; // <-- ADICIONA ESTE IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.ReaderDetailsJpaEntity;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;

// 👇 ADICIONA O IMPORT PARA O REPO INTERNO DO USER
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaUserRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReaderDetailsSqlMapper {

    private final UserSqlMapper userMapper;
    private final GenreSqlMapper genreMapper;

    // 👇 ADICIONA A DEPENDÊNCIA PARA O REPO INTERNO
    private final SpringDataJpaUserRepository userJpaRepo;

    /**
     * Converte a Entidade (JPA) para o Domínio.
     * (Sem alterações aqui, já estava correto)
     */
    public ReaderDetails toDomain(ReaderDetailsJpaEntity entity) {
        if (entity == null) return null;

        ReaderDetails domain = new ReaderDetails();
        domain.setPk(entity.getPk());
        domain.setVersion(entity.getVersion());

        domain.setReaderNumber(entity.getReaderNumber());
        domain.setBirthDate(entity.getBirthDate());
        domain.setPhoneNumber(entity.getPhoneNumber());

        domain.setGdprConsent(entity.isGdprConsent());
        domain.setMarketingConsent(entity.isMarketingConsent());
        domain.setThirdPartySharingConsent(entity.isThirdPartySharingConsent());

        if (entity.getReader() != null) {
            domain.setReader((pt.psoft.g1.psoftg1.usermanagement.model.Reader) userMapper.toDomain(entity.getReader()));
        }

        if (entity.getInterestList() != null) {
            domain.setInterestList(
                    entity.getInterestList().stream()
                            .map(genreMapper::toDomain)
                            .collect(Collectors.toList())
            );
        }

        if (entity.getPhoto() != null) {
            domain.setPhoto(entity.getPhoto().getPhotoFile());
        }

        return domain;
    }

    /**
     * Converte o Domínio para a Entidade (JPA).
     */
    public ReaderDetailsJpaEntity toEntity(ReaderDetails domain) {
        if (domain == null) return null;

        ReaderDetailsJpaEntity entity = new ReaderDetailsJpaEntity();
        entity.setPk(domain.getPk());
        entity.setVersion(domain.getVersion());

        // Mapeia os Value Objects (corrigido)
        entity.setReaderNumber(domain.getReaderNumberObject());
        entity.setBirthDate(domain.getBirthDate());
        entity.setPhoneNumber(domain.getPhoneNumberObject());

        // Mapeia os booleanos
        entity.setGdprConsent(domain.isGdprConsent());
        entity.setMarketingConsent(domain.isMarketingConsent());
        entity.setThirdPartySharingConsent(domain.isThirdPartySharingConsent());

        // --- MAPEAMENTO DE RELAÇÕES CORRIGIDO ---
        if (domain.getReader() != null) {
            // O problema: domain.getReader() é um objeto de Domínio,
            // e o userMapper.toEntity() criaria um *novo* UserJpaEntity transiente.
            // A Solução: Temos de ir buscar a *verdadeira* entidade UserJpaEntity
            // que já está guardada na BD.

            // 1. Obtém o ID do domínio
            String userId = domain.getReader().getId();

            // 2. Vai buscar a Entidade JPA gerida
            // (Se o ID for nulo ou não for encontrado, lança uma exceção)
            pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity managedUserEntity =
                    userJpaRepo.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId + " while mapping ReaderDetails"));

            entity.setReader(managedUserEntity); // <-- Define a entidade gerida
        }

        if (domain.getInterestList() != null) {
            entity.setInterestList(
                    domain.getInterestList().stream()
                            .map(genreMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        entity.setPhoto(domain.getPhoto());

        return entity;
    }
}