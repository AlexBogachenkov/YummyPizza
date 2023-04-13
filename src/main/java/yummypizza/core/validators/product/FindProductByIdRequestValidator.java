package yummypizza.core.validators.product;

import org.springframework.stereotype.Component;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindProductByIdRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(FindProductByIdRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Product ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Product ID", "must be a positive number."));
        }
    }

}
