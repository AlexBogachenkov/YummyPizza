package yummypizza.core.validators.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteProductByIdRequestValidator {

    @Autowired
    private ProductRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(DeleteProductByIdRequest request) {
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
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("Product ID", "doesn't exist."));
        }
    }

}
