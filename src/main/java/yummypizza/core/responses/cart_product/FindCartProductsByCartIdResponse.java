package yummypizza.core.responses.cart_product;

import lombok.Getter;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindCartProductsByCartIdResponse extends CoreResponse {

    private List<CartProduct> cartProducts;

    public FindCartProductsByCartIdResponse(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public FindCartProductsByCartIdResponse() {
    }

}
