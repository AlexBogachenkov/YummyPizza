package yummypizza.core.requests.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindProductByIdRequest {

    private Long id;

    public FindProductByIdRequest(Long id) {
        this.id = id;
    }

}
