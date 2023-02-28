package yummypizza.core.requests.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteUserByIdRequest {

    private Long id;

    public DeleteUserByIdRequest(Long id) {
        this.id = id;
    }

}
