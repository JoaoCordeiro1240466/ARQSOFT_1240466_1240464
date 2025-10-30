package pt.psoft.g1.psoftg1.bookmanagement.model;


// Imports de persistência REMOVIDOS
// import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
// Import do Hibernate REMOVIDO
// import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// @Entity e @Table REMOVIDOS
public class Book extends EntityWithPhoto {

    // @Id e @GeneratedValue REMOVIDOS
    @Getter
    private long pk;

    // @Version REMOVIDO
    @Getter
    private Long version;

    // @Embedded REMOVIDO
    Isbn isbn;

    @Getter
    // @Embedded REMOVIDO
    @NotNull
    Title title;

    @Getter
    // @ManyToOne REMOVIDO
    @NotNull
    Genre genre;

    @Getter
    // @ManyToMany REMOVIDO
    private List<Author> authors = new ArrayList<>();

    // @Embedded REMOVIDO
    Description description;

    public void setTitle(String title) {this.title = new Title(title);}

    public void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    public void setDescription(String description) {this.description = new Description(description); }

    public void setGenre(Genre genre) {this.genre = genre; }

    public void setAuthors(List<Author> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public Book(String isbn, String title, String description, Genre genre, List<Author> authors, String photoURI) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        if(genre==null)
            throw new IllegalArgumentException("Genre cannot be null");
        setGenre(genre);
        if(authors == null)
            throw new IllegalArgumentException("Author list is null");
        if(authors.isEmpty())
            throw new IllegalArgumentException("Author list is empty");

        setAuthors(authors);
        // Atualizado para usar o método público da classe-mãe
        setPhoto(photoURI);
    }

    // Alterado para 'public' para o Mapper
    public Book() {
        // para o Mapper
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        // Atualizado para usar o método público da classe-mãe
        setPhoto(null);
    }

    public void applyPatch(final Long desiredVersion, UpdateBookRequest request) {
        if (!Objects.equals(this.version, desiredVersion))
            // Removida a exceção do Hibernate
            throw new ConflictException("Object was already modified by another user");

        String title = request.getTitle();
        String description = request.getDescription();
        Genre genre = request.getGenreObj();
        List<Author> authors = request.getAuthorObjList();
        String photoURI = request.getPhotoURI();
        if(title != null) {
            setTitle(title);
        }

        if(description != null) {
            setDescription(description);
        }

        if(genre != null) {
            setGenre(genre);
        }

        if(authors != null) {
            setAuthors(authors);
        }

        if(photoURI != null)
            // Atualizado para usar o método público da classe-mãe
            setPhoto(photoURI);
    }

    public String getIsbn(){
        return this.isbn.toString();
    }

    // --- Setters para o Mapper ---
    // Adicionados para que o Mapper consiga construir o objeto a partir da BD
    public void setPk(long pk) {
        this.pk = pk;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}