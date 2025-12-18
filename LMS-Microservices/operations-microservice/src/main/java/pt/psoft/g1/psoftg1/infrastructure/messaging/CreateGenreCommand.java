package pt.psoft.g1.psoftg1.infrastructure.messaging;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateGenreCommand {
    private String genreId;
    private String name;
}
