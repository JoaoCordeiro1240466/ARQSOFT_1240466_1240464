package pt.psoft.g1.psoftg1.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Description {
    @Transient
    private final int DESC_MAX_LENGTH = 4096;

    @Size(max = DESC_MAX_LENGTH)
    @Column(length = DESC_MAX_LENGTH)
    String description;

    public Description(String description) {
        setDescription(description);
    }

    protected Description() {}

    public void setDescription(@Nullable String description) {
        if(description == null || description.isBlank()) {
            this.description = null;
        }else if(description.length() > DESC_MAX_LENGTH) {
            throw new IllegalArgumentException("Description has a maximum of 4096 characters");
        }else{
            // Basic sanitization, consider a more robust library if needed
            this.description = description.strip();
        }
    }

    public String toString() {
        return this.description;
    }
}
