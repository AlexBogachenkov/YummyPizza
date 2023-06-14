package yummypizza.core.validators.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.requests.order.DeleteOrderByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteOrderByIdRequestValidator {

    @Autowired
    private OrderRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(DeleteOrderByIdRequest request) {
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
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("Order ID", "doesn't exist."));
        }
    }

}
