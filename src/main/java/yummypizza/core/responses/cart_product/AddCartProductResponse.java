package yummypizza.core.responses.cart_product;

import lombok.Getter;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class AddCartProductResponse extends CoreResponse {

    private CartProduct cartProductAdded;

    public AddCartProductResponse(List<CoreError> errors) {
        super(errors);
    }

    public AddCartProductResponse(CartProduct cartProductAdded) {
        this.cartProductAdded = cartProductAdded;
    }

}
