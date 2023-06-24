package yummypizza.core.requests.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindOrdersByUserIdRequest {

    private Long userId;

    public FindOrdersByUserIdRequest(Long userId) {
        this.userId = userId;
    }

}
