package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter; // <-- ADICIONADO IMPORT
import lombok.Setter; // <-- ADICIONADO IMPORT


@Embeddable
@Getter // <-- ADICIONADO
@Setter // <-- ADICIONADO
public class Bio {
    @Transient
    private final int BIO_MAX_LENGTH = 4096;

    @Column(nullable = false, length = BIO_MAX_LENGTH)
    @NotNull
    @Size(min = 1, max = BIO_MAX_LENGTH)
    private String bio;

    public Bio(String bio) {
        setBio(bio);
    }

    protected Bio() {
    }

    // O Lombok @Setter não vai sobrepor este método
    public void setBio(String bio) {
        if(bio == null)
            throw new IllegalArgumentException("Bio cannot be null");
        if(bio.isBlank())
            throw new IllegalArgumentException("Bio cannot be blank");
        if(bio.length() > BIO_MAX_LENGTH)
            throw new IllegalArgumentException("Bio has a maximum of 4096 characters");
        this.bio = bio;
    }

    @Override
    public String toString() {
        return bio;
    }
}