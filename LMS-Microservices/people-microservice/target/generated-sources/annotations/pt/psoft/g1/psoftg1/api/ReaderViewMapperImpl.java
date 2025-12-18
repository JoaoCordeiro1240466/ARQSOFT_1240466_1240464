package pt.psoft.g1.psoftg1.api;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.api.views.ReaderView;
import pt.psoft.g1.psoftg1.model.Reader;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T20:09:58+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class ReaderViewMapperImpl implements ReaderViewMapper {

    @Override
    public ReaderView toReaderView(Reader reader) {
        if ( reader == null ) {
            return null;
        }

        ReaderView readerView = new ReaderView();

        readerView.setReaderNumber( reader.getReaderNumber() );
        readerView.setEmail( reader.getEmail() );
        readerView.setFullName( reader.getFullName() );
        readerView.setPhoneNumber( reader.getPhoneNumber() );

        return readerView;
    }

    @Override
    public List<ReaderView> toReaderView(List<Reader> readers) {
        if ( readers == null ) {
            return null;
        }

        List<ReaderView> list = new ArrayList<ReaderView>( readers.size() );
        for ( Reader reader : readers ) {
            list.add( toReaderView( reader ) );
        }

        return list;
    }
}
