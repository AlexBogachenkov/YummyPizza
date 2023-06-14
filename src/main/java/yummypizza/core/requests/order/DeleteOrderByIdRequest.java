package yummypizza.core.requests.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteOrderByIdRequest {

    private Long id;

    public DeleteOrderByIdRequest(Long id) {
        this.id = id;
    }

}
