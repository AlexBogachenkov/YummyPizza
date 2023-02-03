package yummypizza.core.responses;

import lombok.Getter;
import yummypizza.core.domain.User;

import java.util.List;
import java.util.Optional;

@Getter
public class FindUserByIdResponse extends CoreResponse {

    private Optional<User> foundUser;

    public FindUserByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindUserByIdResponse(Optional<User> foundUser) {
        this.foundUser = foundUser;
    }

}
