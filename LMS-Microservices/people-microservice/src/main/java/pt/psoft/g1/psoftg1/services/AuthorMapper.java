package pt.psoft.g1.psoftg1.services;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    // The mapping logic is handled manually in the service for simplicity
    // This interface is kept for consistency and potential future use with MapStruct
}
