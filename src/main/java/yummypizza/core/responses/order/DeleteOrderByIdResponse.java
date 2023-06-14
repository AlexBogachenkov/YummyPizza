package yummypizza.core.responses.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteOrderByIdResponse extends CoreResponse {

    public DeleteOrderByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
