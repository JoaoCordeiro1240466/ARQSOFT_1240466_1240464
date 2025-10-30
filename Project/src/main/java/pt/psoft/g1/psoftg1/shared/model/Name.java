package pt.psoft.g1.psoftg1.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter; // <-- ADICIONADO IMPORT
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter // <-- ADICIONADO
@Embeddable
@PropertySource({"classpath:config/library.properties"})
public class Name {
    @NotNull
    @NotBlank
    @Column(name="NAME", length = 150)
    String name;

    public Name(String name){
        setName(name);
    }

    // O Lombok @Setter não vai sobrepor este método
    public void setName(String name){
        if(name == null)
            throw new IllegalArgumentException("Name cannot be null");
        if(name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank, nor only white spaces");
        if(!StringUtilsCustom.isAlphanumeric(name))
            throw new IllegalArgumentException("Name can only contain alphanumeric characters");
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    protected Name() {
        // for ORM only
    }
}