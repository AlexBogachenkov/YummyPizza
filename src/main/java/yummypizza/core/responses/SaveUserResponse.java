package yummypizza.core.responses;

import lombok.Getter;
import yummypizza.core.domain.User;

import java.util.List;

@Getter
public class SaveUserResponse extends CoreResponse {

    private User savedUser;

    public SaveUserResponse(List<CoreError> errors) {
        super(errors);
    }

    public SaveUserResponse(User savedUser) {
        this.savedUser = savedUser;
    }

}
