package yummypizza.core.responses.product;

import lombok.Getter;
import yummypizza.core.domain.Product;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindAllProductsResponse extends CoreResponse {

    private List<Product> allProducts;

    public FindAllProductsResponse(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

}
