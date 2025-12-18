package pt.psoft.g1.psoftg1.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGenreCommand {
    private String genreId;
    private String name;
}
