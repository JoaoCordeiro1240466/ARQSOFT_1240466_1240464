package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Mapper(componentModel = "spring")
public abstract class LendingMapper {

    // 1. Mapear Entidade -> View (Resposta REST)
    // O compilador diz que a entidade tem 'isbn' e 'readerId'
    @Mapping(target = "bookIsbn", source = "isbn")       // Na View chama-se bookIsbn, na Entidade isbn
    @Mapping(target = "readerCode", source = "readerId") // Na View chama-se readerCode, na Entidade readerId
    public abstract LendingView toLendingView(Lending lending);

    // 2. Mapear Request -> Entidade (Criar na BD)

    // Ignoramos campos gerados automaticamente
    @Mapping(target = "lendingNumber", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "limitDate", ignore = true)
    @Mapping(target = "returnedDate", ignore = true)

    // Removi o ignore do 'fine' porque o erro dizia que o campo 'fine' não existe na entidade!

    // Mapeamentos de campos
    @Mapping(target = "isbn", source = "isbn")             // Request(isbn) -> Entidade(isbn)
    @Mapping(target = "readerId", source = "readerNumber") // Request(readerNumber) -> Entidade(readerId)

    @Mapping(target = "version", ignore = true)
    public abstract Lending createRequestToEntity(CreateLendingRequest request);
}