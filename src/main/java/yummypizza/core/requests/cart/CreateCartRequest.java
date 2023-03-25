package yummypizza.core.requests.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.CartStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {

    private Long userId;
    private CartStatus status;

}
