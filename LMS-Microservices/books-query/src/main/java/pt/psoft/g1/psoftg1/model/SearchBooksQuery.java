package pt.psoft.g1.psoftg1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBooksQuery {
    private String title;
    private String genreId;
    private String authorId;
}
