package yummypizza.core.validators.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddCartProductRequestValidator {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(AddCartProductRequest request) {
        errors = new ArrayList<>();
        validateCartId(request.getCartId());
        validateProductId(request.getProductId());
        validateQuantity(request.getQuantity());
        return errors;
    }

    private void validateCartId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Cart ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Cart ID", "must be a positive number."));
            return;
        }
        if (!cartRepository.existsById(id)) {
            errors.add(new CoreError("Cart ID", "doesn't exist."));
        }
    }

    private void validateProductId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Product ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Product ID", "must be a positive number."));
            return;
        }
        if (!productRepository.existsById(id)) {
            errors.add(new CoreError("Product ID", "doesn't exist."));
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            errors.add(new CoreError("Quantity", "must be a positive number."));
        }
    }

}
