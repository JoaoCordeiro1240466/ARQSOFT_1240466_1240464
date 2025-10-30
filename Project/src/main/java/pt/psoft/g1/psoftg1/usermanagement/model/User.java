/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * (Licença omitida para brevidade)
 *
 */
package pt.psoft.g1.psoftg1.usermanagement.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID; // <-- IMPORT ADICIONADO

// Imports de persistência REMOVIDOS
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.psoft.g1.psoftg1.shared.model.Name;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo de Domínio "User".
 * Agora é um POJO (Plain Old Java Object) limpo, sem conhecimento
 * de como é guardado na base de dados.
 */
// @Entity, @Table, @EntityListeners REMOVIDOS
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    // @Id, @Column REMOVIDOS
    @Getter
    private String id;

    // @Version REMOVIDO
    private Long version;

    // @CreatedDate, @Column REMOVIDOS
    @Getter
    private LocalDateTime createdAt;

    // @LastModifiedDate, @Column REMOVIDOS
    @Getter
    private LocalDateTime modifiedAt;

    // @CreatedBy, @Column REMOVIDOS
    @Getter
    private String createdBy;

    // @LastModifiedBy, @Column REMOVIDO
    private String modifiedBy;

    @Setter
    @Getter
    private boolean enabled = true;

    @Setter
    // @Column REMOVIDO
    @Email
    @Getter
    @NotNull
    @NotBlank
    private String username;

    // @Column REMOVIDO
    @Getter
    @NotNull
    @NotBlank
    private String password;

    @Getter
    // @Embedded REMOVIDO
    private Name name;

    // @ElementCollection REMOVIDO
    @Getter
    private final Set<Role> authorities = new HashSet<>();

    /**
     * Alterado para 'private' para forçar o uso de factory methods,
     * já que não é mais necessário para o ORM.
     */
    protected User() {
        // Apenas para uso interno e frameworks
    }

    /**
     *
     * @param username
     * @param password
     */
    public User(final String username, final String password) {
        this.username = username;
        setPassword(password);
    }

    // O setter do ID é lógica de domínio (usado pelo bootstrapper e gerador)
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Métodos factory mantêm-se, são lógica de domínio.
     */
    public static User newUser(final String username, final String password, final String name) {
        final var u = new User(username, password);
        u.setName(name);
        // ***** CORREÇÃO APLICADA *****
        u.setId(UUID.randomUUID().toString().replace("-", ""));
        return u;
    }

    public static User newUser(final String username, final String password, final String name, final String role) {
        final var u = new User(username, password);
        u.setName(name);
        u.addAuthority(new Role(role));
        // ***** CORREÇÃO APLICADA *****
        u.setId(UUID.randomUUID().toString().replace("-", ""));
        return u;
    }

    // Métodos de lógica de negócio mantêm-se

    public void setPassword(final String password) {
        Password passwordCheck = new Password(password);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

    // Métodos de UserDetails mantêm-se (lógica de domínio de segurança)

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    public void setName(String name){
        this.name = new Name(name);
    }

    // Setters manuais para campos de auditoria (usados pelo mapper)
    // O domínio não os define, mas a infraestrutura pode precisar de os
    // definir ao carregar da BD.

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}