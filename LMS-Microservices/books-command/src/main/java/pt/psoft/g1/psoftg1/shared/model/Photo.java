package pt.psoft.g1.psoftg1.shared.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Photo {

    private String photoFile;

    public Photo(String photoFile) {
        this.photoFile = photoFile;
    }
}
