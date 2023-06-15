package yummypizza.core.responses.cart_product;

import lombok.Getter;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindAllCartProductsResponse extends CoreResponse {

    private List<CartProduct> allCartProducts;

    public FindAllCartProductsResponse(List<CartProduct> allCartProducts) {
        this.allCartProducts = allCartProducts;
    }

}
