package pt.psoft.g1.psoftg1.api;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.api.views.UserView;
import pt.psoft.g1.psoftg1.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T23:10:19+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class UserViewMapperImpl implements UserViewMapper {

    @Override
    public UserView toUserView(User user) {
        if ( user == null ) {
            return null;
        }

        UserView userView = new UserView();

        userView.setId( user.getId() );
        userView.setUsername( user.getUsername() );
        userView.setFullName( user.getFullName() );

        return userView;
    }

    @Override
    public List<UserView> toUserView(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserView> list = new ArrayList<UserView>( users.size() );
        for ( User user : users ) {
            list.add( toUserView( user ) );
        }

        return list;
    }
}
