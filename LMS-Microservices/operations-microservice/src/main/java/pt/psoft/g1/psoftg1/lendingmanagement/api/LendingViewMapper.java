package pt.psoft.g1.psoftg1.lendingmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LendingViewMapper {

    // CORREÇÃO AQUI: source = "isbn" (porque é assim que se chama na Entidade)
    @Mapping(target = "bookIsbn", source = "isbn")

    // Mantém este (a entidade tem readerId, a view tem readerCode)
    @Mapping(target = "readerCode", source = "readerId")

    // Multas
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")

    public abstract LendingView toLendingView(Lending lending);

    public abstract List<LendingView> toLendingView(List<Lending> lendings);
}
