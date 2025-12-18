package pt.psoft.g1.psoftg1.shared.model;

import jakarta.persistence.Embedded;
import lombok.Getter;

@Getter
public abstract class EntityWithPhoto {

    @Embedded
    private Photo photo;

    public void setPhoto(String photoURI) {
        if (photoURI == null || photoURI.isBlank()) {
            this.photo = null;
        } else {
            this.photo = new Photo(photoURI);
        }
    }
}
