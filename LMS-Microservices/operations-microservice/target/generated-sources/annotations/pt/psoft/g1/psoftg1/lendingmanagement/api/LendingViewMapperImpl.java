package pt.psoft.g1.psoftg1.lendingmanagement.api;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-19T00:30:44+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class LendingViewMapperImpl extends LendingViewMapper {

    @Override
    public LendingView toLendingView(Lending lending) {
        if ( lending == null ) {
            return null;
        }

        LendingView lendingView = new LendingView();

        lendingView.setCode( lending.getLendingNumber() );
        lendingView.setBookIsbn( lending.getIsbn() );
        lendingView.setReaderCode( lending.getReaderCode() );
        lendingView.setFineValuePerDayInCents( (double) lending.getFineValuePerDayInCents() );
        lendingView.setLendingNumber( lending.getLendingNumber() );
        lendingView.setStartDate( lending.getStartDate() );
        lendingView.setLimitDate( lending.getLimitDate() );
        lendingView.setReturnedDate( lending.getReturnedDate() );
        lendingView.setCommentary( lending.getCommentary() );

        return lendingView;
    }

    @Override
    public List<LendingView> toLendingView(List<Lending> lendings) {
        if ( lendings == null ) {
            return null;
        }

        List<LendingView> list = new ArrayList<LendingView>( lendings.size() );
        for ( Lending lending : lendings ) {
            list.add( toLendingView( lending ) );
        }

        return list;
    }
}
