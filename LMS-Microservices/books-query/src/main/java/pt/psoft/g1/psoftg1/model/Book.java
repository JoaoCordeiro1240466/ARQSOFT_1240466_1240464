package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private Long version;

    private String isbn;

    @NotNull
    private String title;

    @NotNull
    private String genreId;

    @ElementCollection
    private List<String> authorIds = new ArrayList<>();

    private String description;

    private String photo;

    public Book(String isbn, String title, String description, String genreId, List<String> authorIds, String photo) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.genreId = genreId;
        this.authorIds = authorIds;
        this.photo = photo;
    }
}
