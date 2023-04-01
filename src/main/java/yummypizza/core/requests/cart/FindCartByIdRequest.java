package yummypizza.core.requests.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindCartByIdRequest {

    private Long id;

    public FindCartByIdRequest(Long id) {
        this.id = id;
    }

}
