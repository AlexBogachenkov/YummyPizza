package yummypizza.core.requests.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.CartStatus;

@Data
@NoArgsConstructor
public class UpdateCartRequest {

    private Long id;
    private Long userId;
    private CartStatus status;

    public UpdateCartRequest(Long id, Long userId, CartStatus status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

}
