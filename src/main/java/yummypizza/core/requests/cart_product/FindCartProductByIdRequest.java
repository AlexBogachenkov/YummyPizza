package yummypizza.core.requests.cart_product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindCartProductByIdRequest {

    private Long id;

    public FindCartProductByIdRequest(Long id) {
        this.id = id;
    }

}
