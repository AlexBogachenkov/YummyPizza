package yummypizza.core.validators.order;

import org.springframework.stereotype.Component;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindOrderByIdRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(FindOrderByIdRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Order ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Order ID", "must be a positive number."));
        }
    }

}
