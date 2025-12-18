package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import pt.psoft.g1.psoftg1.services.UpdateAuthorRequest;

@Entity
public class Author {

    @Id
    @GeneratedValue
    @Getter
    private Long authorNumber;

    @Version
    private long version;

    private String name;

    private String bio;

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getVersion() {
        return version;
    }

    public Long getId() {
        return authorNumber;
    }

    public Author(String name, String bio) {
        setName(name);
        setBio(bio);
    }

    public Author() {
        // para ORM apenas
    }


    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            throw new IllegalArgumentException("Object was already modified by another user");

        if (request.getName() != null)
            setName(request.getName());
        if (request.getBio() != null)
            setBio(request.getBio());
    }

    public String getName() {
        return this.name;
    }

    public String getBio() {
        return this.bio;
    }


    public void setAuthorNumber(Long authorNumber) {
        this.authorNumber = authorNumber;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
