package yummypizza.core.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindUserByIdRequest {

    private Long id;

    public FindUserByIdRequest(Long id) {
        this.id = id;
    }

}
