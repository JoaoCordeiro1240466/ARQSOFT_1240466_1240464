package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter; // <-- ADICIONADO IMPORT

@Embeddable
@Getter // <-- ADICIONADO AO NÍVEL DA CLASSE
@Setter // <-- ADICIONADO AO NÍVEL DA CLASSE
public class Title {
    @Transient
    private final int TITLE_MAX_LENGTH = 128;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = TITLE_MAX_LENGTH)
    @Column(name="TITLE", length = TITLE_MAX_LENGTH)
    // @Getter (removido do campo, agora está na classe)
    String title;

    protected Title() {}

    public Title(String title) {
        setTitle(title);
    }

    // O Lombok @Setter não vai sobrepor este método
    // porque ele já existe (tem lógica customizada)
    public void setTitle(String title) {

/*
        if (!StringUtilsCustom.startsOrEndsInWhiteSpace(title)) {
            throw new IllegalArgumentException("Invalid title: " + title);
        }
*/
        if(title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if(title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        if(title.length() > TITLE_MAX_LENGTH)
            throw new IllegalArgumentException("Title has a maximum of " + TITLE_MAX_LENGTH + " characters");
        this.title = title.strip();
    }

    public String toString() {
        return this.title;
    }
}