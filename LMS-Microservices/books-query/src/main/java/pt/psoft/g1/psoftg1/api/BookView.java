package pt.psoft.g1.psoftg1.api;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BookView {
    private String isbn;
    private String title;
    private String description;
    private String genre;
    private List<String> authors;
    private Map<String, Object> _links;
}
