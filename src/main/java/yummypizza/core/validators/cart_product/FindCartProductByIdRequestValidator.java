package yummypizza.core.validators.cart_product;

import org.springframework.stereotype.Component;
import yummypizza.core.requests.cart_product.FindCartProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindCartProductByIdRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(FindCartProductByIdRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Cart product ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Cart product ID", "must be a positive number."));
        }
    }

}
