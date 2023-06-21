package yummypizza.core.validators.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartRepository;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindCartProductsByCartIdRequestValidator {

    @Autowired
    private CartRepository cartRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(FindCartProductsByCartIdRequest request) {
        errors = new ArrayList<>();
        validateCartId(request.getCartId());
        return errors;
    }

    private void validateCartId(Long cartId) {
        if (cartId == null) {
            errors.add(new CoreError("Cart ID", "is mandatory."));
            return;
        }
        if (cartId <= 0) {
            errors.add(new CoreError("Cart ID", "must be a positive number."));
        }
        if (!cartRepository.existsById(cartId)) {
            errors.add(new CoreError("Cart ID", "doesn't exist."));
        }

    }

}
