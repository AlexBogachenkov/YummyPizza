package yummypizza.core.responses.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteUserByIdResponse extends CoreResponse {

    public DeleteUserByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
