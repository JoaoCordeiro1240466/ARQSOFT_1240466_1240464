package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.services.dtos.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.events.CreateGenreCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private long pk;

    @Version
    private Long version;

    @NotNull
    private Isbn isbn;

    @NotNull
    private Title title;

    @NotNull
    private String genreId;

    @ElementCollection
    private List<String> authorIds = new ArrayList<>();

    private Description description;

    public Book(String isbn, String title, String description, String genreId, List<String> authorIds) {
        this.isbn = new Isbn(isbn);
        this.title = new Title(title);
        if (description != null) {
            this.description = new Description(description);
        }
        if (genreId == null) {
            throw new IllegalArgumentException("Genre ID cannot be null");
        }
        this.genreId = genreId;
        if (authorIds == null || authorIds.isEmpty()) {
            throw new IllegalArgumentException("Author ID list cannot be null or empty");
        }
        this.authorIds = authorIds;
    }

    public void applyPatch(final Long desiredVersion, UpdateBookRequest request) {
        if (!Objects.equals(this.version, desiredVersion)) {
            throw new ConflictException("Object was already modified by another user");
        }

        if (request.getTitle() != null) {
            this.title = new Title(request.getTitle());
        }

        if (request.getDescription() != null) {
            this.description = new Description(request.getDescription());
        }

        if (request.getGenreId() != null) {
            this.genreId = request.getGenreId();
        }

        if (request.getAuthorIds() != null) {
            this.authorIds = request.getAuthorIds();
        }
    }
}
