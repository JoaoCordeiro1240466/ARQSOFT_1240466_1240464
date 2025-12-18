package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class UpdateBookRequest {
    @Setter
    private String isbn;

    @Setter
    private String description;

    private String title;

    @Nullable
    @Setter
    private String photoURI;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    private String genreId;

    private List<String> authorIds;

    public UpdateBookRequest(String isbn, String title, String genreId, List<String> authorIds, String description) {
        this.isbn = isbn;
        this.genreId = genreId;
        this.title = title;
        this.description = description;
        this.authorIds = authorIds;
    }

    // The fields genreObj, authorObjList, genre, and authors have been replaced
    // by genreId and authorIds to align with the Book entity refactoring.
}
