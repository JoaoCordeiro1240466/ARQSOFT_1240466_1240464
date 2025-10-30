package pt.psoft.g1.psoftg1.authormanagement.model;

// Imports de persistência REMOVIDOS
// import jakarta.persistence.*;
import lombok.Getter;
// Import do Hibernate REMOVIDO
// import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Name;

// @Entity REMOVIDO
public class Author extends EntityWithPhoto {

    // @Id, @GeneratedValue, @Column REMOVIDOS
    @Getter
    private Long authorNumber;

    // @Version REMOVIDO
    private long version;

    // @Embedded REMOVIDO
    private Name name;

    // @Embedded REMOVIDO
    private Bio bio;

    public void setName(String name) {
        this.name = new Name(name);
    }

    public void setBio(String bio) {
        this.bio = new Bio(bio);
    }

    public Long getVersion() {
        return version;
    }

    public Long getId() {
        return authorNumber;
    }

    public Author(String name, String bio, String photoURI) {
        setName(name);
        setBio(bio);
        // Atualizado para usar o método público da classe-mãe
        setPhoto(photoURI);
    }

    public Author() {
        // para ORM apenas
    }


    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (this.version != desiredVersion)
            // Substituído o StaleObjectStateException (do Hibernate)
            // por uma exceção de domínio
            throw new ConflictException("Object was already modified by another user");

        if (request.getName() != null)
            setName(request.getName());
        if (request.getBio() != null)
            setBio(request.getBio());
        if(request.getPhotoURI() != null)
            // Atualizado para usar o método público da classe-mãe
            setPhoto(request.getPhotoURI());
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        // Atualizado para usar o método público da classe-mãe
        setPhoto(null);
    }
    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }


    public void setAuthorNumber(Long authorNumber) {
        this.authorNumber = authorNumber;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}