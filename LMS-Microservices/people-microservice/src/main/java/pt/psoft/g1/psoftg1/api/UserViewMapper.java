package pt.psoft.g1.psoftg1.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.api.views.UserView;
import pt.psoft.g1.psoftg1.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserViewMapper {

    UserView toUserView(User user);

    List<UserView> toUserView(List<User> users);
}
