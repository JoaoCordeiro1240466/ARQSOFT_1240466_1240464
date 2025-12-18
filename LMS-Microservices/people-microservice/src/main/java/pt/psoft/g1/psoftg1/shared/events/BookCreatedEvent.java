package pt.psoft.g1.psoftg1.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreatedEvent {
    private String isbn;
    private String title;
    private String description;
    private String authorId;
    private String genreId;
}
