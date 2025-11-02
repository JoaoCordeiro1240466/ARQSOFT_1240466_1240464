package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity;


import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import java.util.Set;

@Component
public class UserSqlMapper {

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getAuthorities();
        User domainUser;

        if (roles.contains(new Role(Role.READER))) {
            domainUser = new Reader(entity.getUsername(), entity.getPassword());

        } else if (roles.contains(new Role(Role.LIBRARIAN))) {
            domainUser = new Librarian(entity.getUsername(), entity.getPassword());

        } else {
            domainUser = new User(entity.getUsername(), entity.getPassword());
        }

        domainUser.setId(entity.getId());
        domainUser.setName(entity.getName().getName());
        domainUser.setEnabled(entity.isEnabled());

        domainUser.getAuthorities().clear();
        if (roles != null) {
            for (Role role : roles) {
                domainUser.addAuthority(role);
            }
        }

        domainUser.setVersion(entity.getVersion());
        domainUser.setCreatedAt(entity.getCreatedAt());
        domainUser.setModifiedAt(entity.getModifiedAt());
        domainUser.setCreatedBy(entity.getCreatedBy());
        domainUser.setModifiedBy(entity.getModifiedBy());

        return domainUser;
    }

    public UserJpaEntity toEntity(User domainUser) {
        if (domainUser == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();

        entity.setId(domainUser.getId());
        entity.setUsername(domainUser.getUsername());
        entity.setPassword(domainUser.getPassword());
        entity.setName(domainUser.getName());
        entity.setEnabled(domainUser.isEnabled());

        entity.getAuthorities().clear();
        for (Role role : domainUser.getAuthorities()) {
            entity.getAuthorities().add(role);
        }

        return entity;
    }
}