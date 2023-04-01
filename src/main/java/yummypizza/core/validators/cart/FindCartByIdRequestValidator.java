package yummypizza.core.validators.cart;

import org.springframework.stereotype.Component;
import yummypizza.core.requests.cart.FindCartByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindCartByIdRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(FindCartByIdRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Cart ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Cart ID", "must be a positive number."));
        }
    }

}
