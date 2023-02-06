package yummypizza.core.responses;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteUserByIdResponse extends CoreResponse {

    private boolean isUserDeleted;

    public DeleteUserByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public DeleteUserByIdResponse(boolean isUserDeleted) {
        this.isUserDeleted = isUserDeleted;
    }

}
