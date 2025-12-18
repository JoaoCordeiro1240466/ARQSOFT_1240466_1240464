package pt.psoft.g1.psoftg1.services.dtos;

import lombok.AllArgsConstructor; // <--- Novo Import
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // <--- Adiciona isto aqui
public class CreateCompositeBookRequest {

    private String isbn;
    private String title;
    private String description;

    // O nome 'author' tem de ser igual à chave no JSON
    private CreateAuthorRequest author;

    private String genre;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor // <--- E adiciona isto aqui também
    public static class CreateAuthorRequest {
        private String name;
        private String bio;
    }
}