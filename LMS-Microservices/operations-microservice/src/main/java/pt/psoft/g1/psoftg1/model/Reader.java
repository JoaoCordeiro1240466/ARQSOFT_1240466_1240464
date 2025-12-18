package pt.psoft.g1.psoftg1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long databaseId; // Corresponds to the ID from the people-microservice
    private String username;
    private String fullName;
    private String role;

    public Reader(Long databaseId, String username, String fullName, String role) {
        this.databaseId = databaseId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }
}
