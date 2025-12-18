package pt.psoft.g1.psoftg1.shared.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityWithPhoto {

    private String photo;
}
