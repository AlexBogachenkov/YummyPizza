package yummypizza.core.responses.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.Cart;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class FindCartsByUserIdAndStatusResponse extends CoreResponse {

    private List<Cart> carts;

    public FindCartsByUserIdAndStatusResponse(List<Cart> carts) {
        this.carts = carts;
    }

}
