package yummypizza.core.responses.user;

import lombok.Getter;
import yummypizza.core.domain.User;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class UpdateUserResponse extends CoreResponse {

    private User updatedUser;

    public UpdateUserResponse(List<CoreError> errors) {
        super(errors);
    }

    public UpdateUserResponse(User updatedUser) {
        this.updatedUser = updatedUser;
    }

}
