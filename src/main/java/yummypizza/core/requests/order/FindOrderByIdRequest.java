package yummypizza.core.requests.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindOrderByIdRequest {

    private Long id;

    public FindOrderByIdRequest(Long id) {
        this.id = id;
    }

}
