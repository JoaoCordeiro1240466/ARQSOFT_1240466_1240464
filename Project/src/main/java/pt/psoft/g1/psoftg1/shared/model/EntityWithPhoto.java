package pt.psoft.g1.psoftg1.shared.model;

import jakarta.annotation.Nullable;
// Imports de persistência REMOVIDOS
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.MappedSuperclass;
// import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Getter
// @MappedSuperclass REMOVIDO
public abstract class EntityWithPhoto {

    @Nullable
    // @OneToOne e @JoinColumn REMOVIDOS
    protected Photo photo;

    // Esta é a interface pública para definir a foto.
    // O domínio 'Author' deve usar este método.
    public void setPhoto(String photoUri) {
        this.setPhotoInternal(photoUri);
    }

    // A lógica de validação permanece no domínio
    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            try {
                //Se o objeto Path for instanciado com sucesso,
                //temos um Path válido
                this.photo = new Photo(Path.of(photoURI));
            } catch (InvalidPathException e) {
                //Por alguma razão falhou, definimos como nulo
                //para evitar referências inválidas
                this.photo = null;
            }
        }
    }
}