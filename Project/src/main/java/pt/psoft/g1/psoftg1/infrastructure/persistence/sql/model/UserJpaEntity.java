package pt.psoft.g1.psoftg1.infrastructure.persistence.sql.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

/**
 * Esta é a Entidade de Persistência (Data Model) para o User.
 * Ela mapeia 1-para-1 com a tabela T_USER e é usada apenas
 * pela camada de infraestrutura.
 * Contém todas as anotações JPA que removemos do modelo de domínio.
 */
@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
@Getter // Lombok para getters
@Setter // Lombok para setters
@NoArgsConstructor// Construtor vazio para o JPA
public class UserJpaEntity {

    @Id
    @Column(name = "USER_ID", length = 32, updatable = false, nullable = false)
    private String id;

    @Version
    private Long version;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;

    private boolean enabled = true;

    @Column(unique = true, nullable = false)
    @Email
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Assumindo que 'Name' é uma classe anotada com @Embeddable
     */
    @Embedded
    private Name name;

    /**
     * Assumindo que 'Role' é uma classe anotada com @Embeddable
     */
    @ElementCollection(fetch = FetchType.EAGER) // Eager para corresponder ao original
    @CollectionTable(name = "T_USER_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    private final Set<Role> authorities = new HashSet<>();

    // Nota: Não precisamos de lógica de negócio aqui (ex: setPassword com encode).
    // Isso é feito no domínio antes de o mapper converter para esta entidade.
    // Esta classe é apenas um "contentor" de dados para o JPA.
}