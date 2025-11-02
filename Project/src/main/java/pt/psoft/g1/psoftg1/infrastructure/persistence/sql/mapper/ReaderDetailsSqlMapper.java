package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.ReaderDetailsJpaEntity;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.repo.SpringDataJpaUserRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReaderDetailsSqlMapper {

    private final UserSqlMapper userMapper;
    private final GenreSqlMapper genreMapper;
    private final SpringDataJpaUserRepository userJpaRepo;
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

    public ReaderDetailsJpaEntity toEntity(ReaderDetails domain) {
        if (domain == null) return null;

        ReaderDetailsJpaEntity entity = new ReaderDetailsJpaEntity();
        entity.setPk(domain.getPk());
        entity.setVersion(domain.getVersion());

        entity.setReaderNumber(domain.getReaderNumberObject());
        entity.setBirthDate(domain.getBirthDate());
        entity.setPhoneNumber(domain.getPhoneNumberObject());

        entity.setGdprConsent(domain.isGdprConsent());
        entity.setMarketingConsent(domain.isMarketingConsent());
        entity.setThirdPartySharingConsent(domain.isThirdPartySharingConsent());

        if (domain.getReader() != null) {
            String userId = domain.getReader().getId();

            pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity managedUserEntity =
                    userJpaRepo.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId + " while mapping ReaderDetails"));

            entity.setReader(managedUserEntity);
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