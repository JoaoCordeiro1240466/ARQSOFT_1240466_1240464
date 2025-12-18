package pt.psoft.g1.psoftg1.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookView {

    private String isbn;
    private String title;
    private String description;
    private String genreId;
    private List<String> authorIds;
    private String photoURI;
}
