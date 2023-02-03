package yummypizza.core.responses;

import yummypizza.core.domain.User;

import java.util.List;
import java.util.Optional;

public class FindUserByIdResponse extends CoreResponse {

    private Optional<User> foundUser;

    public FindUserByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindUserByIdResponse(Optional<User> foundUser) {
        this.foundUser = foundUser;
    }

}
