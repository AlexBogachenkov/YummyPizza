package yummypizza.core.responses;

import lombok.Getter;
import yummypizza.core.domain.User;

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
