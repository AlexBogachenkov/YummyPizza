package yummypizza.core.responses.product;

import lombok.Getter;
import yummypizza.core.domain.Product;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class UpdateProductResponse extends CoreResponse {

    private Product updatedProduct;

    public UpdateProductResponse(List<CoreError> errors) {
        super(errors);
    }

    public UpdateProductResponse(Product updatedProduct) {
        this.updatedProduct = updatedProduct;
    }

}
