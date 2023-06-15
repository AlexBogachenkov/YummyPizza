package yummypizza.core.requests.cart_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartProductRequest {

    private Long cartId;
    private Long productId;
    private int quantity;

}
