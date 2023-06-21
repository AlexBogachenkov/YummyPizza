package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.FindCartProductsByCartIdResponse;
import yummypizza.core.validators.cart_product.FindCartProductsByCartIdRequestValidator;

import java.util.List;

@Service
public class FindCartProductsByCartIdService {

    @Autowired
    private FindCartProductsByCartIdRequestValidator validator;
    @Autowired
    private CartProductRepository repository;

    public FindCartProductsByCartIdResponse execute(FindCartProductsByCartIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            FindCartProductsByCartIdResponse response = new FindCartProductsByCartIdResponse();
            response.setErrors(errors);
            return response;
        }
        List<CartProduct> cartProducts = repository.findByCartId(request.getCartId());
        return new FindCartProductsByCartIdResponse(cartProducts);
    }

}
