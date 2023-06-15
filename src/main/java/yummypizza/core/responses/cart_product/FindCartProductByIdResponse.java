package yummypizza.core.responses.cart_product;

import lombok.Getter;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;
import java.util.Optional;

@Getter
public class FindCartProductByIdResponse extends CoreResponse {

    private Optional<CartProduct> foundCartProduct;

    public FindCartProductByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindCartProductByIdResponse(Optional<CartProduct> foundCartProduct) {
        this.foundCartProduct = foundCartProduct;
    }

}
