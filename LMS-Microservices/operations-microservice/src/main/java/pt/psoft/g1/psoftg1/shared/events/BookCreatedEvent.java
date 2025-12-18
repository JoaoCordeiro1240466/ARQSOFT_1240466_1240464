package pt.psoft.g1.psoftg1.shared.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreatedEvent {
    private String isbn;
    private String title;
    private String authorId;
    private String genre;
    private int publicationYear;
}
