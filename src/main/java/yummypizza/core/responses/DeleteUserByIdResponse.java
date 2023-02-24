package yummypizza.core.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteUserByIdResponse extends CoreResponse {

    public DeleteUserByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
