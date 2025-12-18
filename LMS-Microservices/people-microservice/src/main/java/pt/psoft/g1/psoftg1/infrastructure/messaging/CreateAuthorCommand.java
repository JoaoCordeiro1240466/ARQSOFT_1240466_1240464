package pt.psoft.g1.psoftg1.infrastructure.messaging;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAuthorCommand {
    private String authorId;
    private String name;
    private String bio;
}
