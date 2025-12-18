package pt.psoft.g1.psoftg1.shared.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreatedEvent {
    private Long databaseId;
    private String username;
    private String fullName;
    private String role;
}
