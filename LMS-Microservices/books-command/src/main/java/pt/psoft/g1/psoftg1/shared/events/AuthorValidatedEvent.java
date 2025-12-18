package pt.psoft.g1.psoftg1.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorValidatedEvent {

    private String isbn;

    private boolean isValid;

    private String message;
}
