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

@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
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

    @Embedded
    private Name name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_USER_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    private final Set<Role> authorities = new HashSet<>();
}