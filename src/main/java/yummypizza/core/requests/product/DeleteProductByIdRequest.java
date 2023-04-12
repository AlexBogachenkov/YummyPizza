package yummypizza.core.requests.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteProductByIdRequest {

    private Long id;

    public DeleteProductByIdRequest(Long id) {
        this.id = id;
    }

}
