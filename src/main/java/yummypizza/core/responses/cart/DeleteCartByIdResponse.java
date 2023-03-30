package yummypizza.core.responses.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteCartByIdResponse extends CoreResponse {

    public DeleteCartByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
