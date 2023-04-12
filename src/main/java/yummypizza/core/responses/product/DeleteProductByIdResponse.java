package yummypizza.core.responses.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteProductByIdResponse extends CoreResponse {

    public DeleteProductByIdResponse(List<CoreError> errors) {
        super(errors);
    }

}
