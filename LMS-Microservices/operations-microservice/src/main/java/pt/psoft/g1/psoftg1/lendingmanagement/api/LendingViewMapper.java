package pt.psoft.g1.psoftg1.lendingmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LendingViewMapper {

    // CORREÇÃO: Mapeia 'code' diretamente para o 'lendingNumber' (o UUID)
    @Mapping(target = "code", source = "lendingNumber")

    // Mapeamentos existentes
    @Mapping(target = "bookIsbn", source = "isbn")
    @Mapping(target = "readerCode", source = "readerCode") // Usa o getter getReaderCode() que vi na sua entidade
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    public abstract LendingView toLendingView(Lending lending);

    public abstract List<LendingView> toLendingView(List<Lending> lendings);
}