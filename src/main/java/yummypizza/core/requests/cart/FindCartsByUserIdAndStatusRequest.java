package yummypizza.core.requests.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.CartStatus;

@Data
@NoArgsConstructor
public class FindCartsByUserIdAndStatusRequest {

    private Long userId;
    private CartStatus cartStatus;

    public FindCartsByUserIdAndStatusRequest(Long userId, CartStatus cartStatus) {
        this.userId = userId;
        this.cartStatus = cartStatus;
    }

}
