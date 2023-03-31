package yummypizza.core.responses.cart;

import lombok.Getter;
import yummypizza.core.domain.Cart;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindAllCartsResponse extends CoreResponse {

    private List<Cart> allCarts;

    public FindAllCartsResponse(List<Cart> allCarts) {
        this.allCarts = allCarts;
    }

}
