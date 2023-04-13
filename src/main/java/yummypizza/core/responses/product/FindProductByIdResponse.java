package yummypizza.core.responses.product;

import lombok.Getter;
import yummypizza.core.domain.Product;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;
import java.util.Optional;

@Getter
public class FindProductByIdResponse extends CoreResponse {

    private Optional<Product> foundProduct;

    public FindProductByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindProductByIdResponse(Optional<Product> foundProduct) {
        this.foundProduct = foundProduct;
    }

}
