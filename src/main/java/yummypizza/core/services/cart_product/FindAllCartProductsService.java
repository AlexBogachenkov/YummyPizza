package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.responses.cart_product.FindAllCartProductsResponse;

@Service
public class FindAllCartProductsService {

    @Autowired
    private CartProductRepository repository;

    public FindAllCartProductsResponse execute() {
        return new FindAllCartProductsResponse(repository.findAll());
    }

}
