package yummypizza.core.requests.cart_product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCartProductByIdRequest {

    private Long id;

    public DeleteCartProductByIdRequest(Long id) {
        this.id = id;
    }

}
