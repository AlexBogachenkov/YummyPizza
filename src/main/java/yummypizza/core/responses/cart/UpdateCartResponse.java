package yummypizza.core.responses.cart;

import lombok.Getter;
import yummypizza.core.domain.Cart;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class UpdateCartResponse extends CoreResponse {

    private Cart updatedCart;

    public UpdateCartResponse(List<CoreError> errors) {
        super(errors);
    }

    public UpdateCartResponse(Cart updatedCart) {
        this.updatedCart = updatedCart;
    }

}
