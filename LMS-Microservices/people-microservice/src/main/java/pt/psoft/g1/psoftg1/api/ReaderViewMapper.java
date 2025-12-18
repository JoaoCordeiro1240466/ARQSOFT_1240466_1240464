package pt.psoft.g1.psoftg1.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.api.views.ReaderView;
import pt.psoft.g1.psoftg1.model.Reader;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReaderViewMapper {
    ReaderView toReaderView(Reader reader);
    List<ReaderView> toReaderView(List<Reader> readers);
}
