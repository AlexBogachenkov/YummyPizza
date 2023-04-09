package yummypizza.core.responses.product;

import lombok.Getter;
import yummypizza.core.domain.Product;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class CreateProductResponse extends CoreResponse {

    private Product createdProduct;

    public CreateProductResponse(List<CoreError> errors) {
        super(errors);
    }

    public CreateProductResponse(Product createdProduct) {
        this.createdProduct = createdProduct;
    }

}
