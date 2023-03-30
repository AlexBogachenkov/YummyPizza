package yummypizza.core.requests.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCartByIdRequest {

    private Long id;

    public DeleteCartByIdRequest(Long id) {
        this.id = id;
    }

}
