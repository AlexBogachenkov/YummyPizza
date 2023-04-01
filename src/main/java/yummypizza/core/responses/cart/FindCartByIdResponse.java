package yummypizza.core.responses.cart;

import lombok.Getter;
import yummypizza.core.domain.Cart;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;
import java.util.Optional;

@Getter
public class FindCartByIdResponse extends CoreResponse {

    private Optional<Cart> foundCart;

    public FindCartByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindCartByIdResponse(Optional<Cart> foundCart) {
        this.foundCart = foundCart;
    }

}
