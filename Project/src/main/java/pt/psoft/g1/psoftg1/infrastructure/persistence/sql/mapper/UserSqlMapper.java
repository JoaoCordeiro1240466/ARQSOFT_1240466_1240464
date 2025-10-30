package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.mapper;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model.UserJpaEntity;


import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import java.util.Set; // <-- ADICIONA ESTE IMPORT

/**
 * Mapper (Tradutor) entre o modelo de domínio User e a entidade JPA UserJpaEntity.
 */
@Component
public class UserSqlMapper {

    /**
     * Converte a entidade da Base de Dados (JPA) para o Modelo de Domínio.
     * Isto é usado quando LÊS da base de dados.
     *
     * @param entity A entidade vinda do JPA
     * @return O modelo de domínio User (ou Reader, ou Librarian)
     */
    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getAuthorities();
        User domainUser; // Declara a variável como a classe base

        // --- LÓGICA DE DECISÃO CORRIGIDA ---
        // Verifica as 'roles' para instanciar a subclasse correta.

        if (roles.contains(new Role(Role.READER))) {
            // Usa o construtor do Reader (que já define a role 'READER')
            domainUser = new Reader(entity.getUsername(), entity.getPassword());

        } else if (roles.contains(new Role(Role.LIBRARIAN))) {
            // Usa o construtor do Librarian (que já define a role 'LIBRARIAN')
            domainUser = new Librarian(entity.getUsername(), entity.getPassword());

        } else {
            // Fallback para um User base (ex: Admin ou outro)
            domainUser = new User(entity.getUsername(), entity.getPassword());
        }
        // --- FIM DA LÓGICA DE DECISÃO ---


        // Agora, preenche os campos comuns que não estão no construtor

        domainUser.setId(entity.getId());
        domainUser.setName(entity.getName().getName()); // (Assumindo que o campo em Name é 'name')
        domainUser.setEnabled(entity.isEnabled());

        // Garante que as 'authorities' (roles) estão 100% corretas
        // (embora os construtores de Reader/Librarian já as devam ter definido)
        domainUser.getAuthorities().clear();
        if (roles != null) {
            for (Role role : roles) {
                domainUser.addAuthority(role);
            }
        }

        // Copia os campos de auditoria e versão
        domainUser.setVersion(entity.getVersion());
        domainUser.setCreatedAt(entity.getCreatedAt());
        domainUser.setModifiedAt(entity.getModifiedAt());
        domainUser.setCreatedBy(entity.getCreatedBy());
        domainUser.setModifiedBy(entity.getModifiedBy());

        // Agora retorna um objeto do tipo correto (Reader, Librarian, etc.)
        return domainUser;
    }

    /**
     * Converte o Modelo de Domínio para a Entidade da Base de Dados (JPA).
     * (Este método já estava correto e não precisa de alterações)
     *
     * @param domainUser O modelo de domínio vindo do serviço
     * @return A entidade JPA pronta para ser guardada
     */
    public UserJpaEntity toEntity(User domainUser) {
        if (domainUser == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();

        // Mapeia os campos
        entity.setId(domainUser.getId());
        entity.setUsername(domainUser.getUsername());
        entity.setPassword(domainUser.getPassword()); // A password já vem encriptada do domínio
        entity.setName(domainUser.getName());
        entity.setEnabled(domainUser.isEnabled());

        // Copia as 'authorities'
        entity.getAuthorities().clear(); // Limpa para o caso de ser uma atualização
        for (Role role : domainUser.getAuthorities()) {
            entity.getAuthorities().add(role);
        }

        // Os campos de auditoria (@CreatedDate, @Version, etc.)
        // são geridos automaticamente pelo AuditingEntityListener do JPA
        // quando a entidade é guardada. Não precisamos de os mapear aqui.

        return entity;
    }
}