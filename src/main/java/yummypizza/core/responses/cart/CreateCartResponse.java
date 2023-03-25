package yummypizza.core.responses.cart;

import lombok.Getter;
import yummypizza.core.domain.Cart;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class CreateCartResponse extends CoreResponse {

    private Cart cartCreated;

    public CreateCartResponse(List<CoreError> errors) {
        super(errors);
    }

    public CreateCartResponse(Cart cartCreated) {
        this.cartCreated = cartCreated;
    }

}
