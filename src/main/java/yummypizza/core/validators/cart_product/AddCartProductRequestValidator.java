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
        return errors;
    }

    private void validateCartId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Groza ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Groza ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!cartRepository.existsById(id)) {
            errors.add(new CoreError("Grozs", "ar šādu ID netika atrasts"));
        }
    }

    private void validateProductId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Produkta ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Produkta ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!productRepository.existsById(id)) {
            errors.add(new CoreError("Produkts", "ar šādu ID netika atrasts"));
        }
    }

}
