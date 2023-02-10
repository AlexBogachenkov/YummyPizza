package yummypizza.core.responses;

import lombok.Getter;
import yummypizza.core.domain.User;

import java.util.List;

@Getter
public class CreateUserResponse extends CoreResponse {

    private User createdUser;

    public CreateUserResponse(List<CoreError> errors) {
        super(errors);
    }

    public CreateUserResponse(User createdUser) {
        this.createdUser = createdUser;
    }

}
