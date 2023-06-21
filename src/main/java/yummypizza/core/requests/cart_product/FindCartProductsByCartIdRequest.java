package yummypizza.core.requests.cart_product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindCartProductsByCartIdRequest {

    private Long cartId;

    public FindCartProductsByCartIdRequest(Long cartId) {
        this.cartId = cartId;
    }

}
