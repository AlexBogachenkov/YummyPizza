package yummypizza.core.requests.cart_product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCartProductRequest {

    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;

    public UpdateCartProductRequest(Long id, Long cartId, Long productId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

}
