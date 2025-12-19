package pt.psoft.g1.psoftg1.lendingmanagement.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-19T00:30:44+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class LendingMapperImpl extends LendingMapper {

    @Override
    public LendingView toLendingView(Lending lending) {
        if ( lending == null ) {
            return null;
        }

        LendingView lendingView = new LendingView();

        lendingView.setBookIsbn( lending.getIsbn() );
        lendingView.setReaderCode( lending.getReaderId() );
        lendingView.setLendingNumber( lending.getLendingNumber() );
        lendingView.setStartDate( lending.getStartDate() );
        lendingView.setLimitDate( lending.getLimitDate() );
        lendingView.setReturnedDate( lending.getReturnedDate() );
        lendingView.setFineValuePerDayInCents( (double) lending.getFineValuePerDayInCents() );
        lendingView.setCommentary( lending.getCommentary() );

        return lendingView;
    }

    @Override
    public Lending createRequestToEntity(CreateLendingRequest request) {
        if ( request == null ) {
            return null;
        }

        Lending lending = new Lending();

        lending.setIsbn( request.getIsbn() );
        lending.setReaderId( request.getReaderNumber() );

        return lending;
    }
}
