package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.requests.cart_product.FindCartProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.FindCartProductByIdResponse;
import yummypizza.core.validators.cart_product.FindCartProductByIdRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FindCartProductByIdService {

    @Autowired
    private FindCartProductByIdRequestValidator validator;
    @Autowired
    private CartProductRepository repository;

    public FindCartProductByIdResponse execute(FindCartProductByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindCartProductByIdResponse(errors);
        }
        Optional<CartProduct> foundCartProduct = repository.findById(request.getId());
        return new FindCartProductByIdResponse(foundCartProduct);
    }

}
