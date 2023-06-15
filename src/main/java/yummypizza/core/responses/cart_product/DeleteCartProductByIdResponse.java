package yummypizza.core.responses.cart_product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteCartProductByIdResponse extends CoreResponse {

    public DeleteCartProductByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
