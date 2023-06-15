package yummypizza.core.validators.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteCartProductByIdRequestValidator {

    @Autowired
    private CartProductRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(DeleteCartProductByIdRequest request) {
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
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("Cart product ID", "doesn't exist."));
        }
    }

}
