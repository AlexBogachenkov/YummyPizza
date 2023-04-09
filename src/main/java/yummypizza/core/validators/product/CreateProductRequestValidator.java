package yummypizza.core.validators.product;

import org.springframework.stereotype.Component;
import yummypizza.core.domain.ProductType;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateProductRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(CreateProductRequest request) {
        errors = new ArrayList<>();
        validateName(request.getName());
        validateDescription(request.getDescription());
        validatePrice(request.getPrice());
        validateType(request.getType());
        return errors;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            errors.add(new CoreError("Name", "is mandatory."));
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            errors.add(new CoreError("Description", "is mandatory."));
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            errors.add(new CoreError("Price", "is mandatory."));
            return;
        }
        if (price.compareTo(new BigDecimal(0)) < 0) {
            errors.add(new CoreError("Price", "must be positive."));
        }
    }

    private void validateType(ProductType type) {
        if (type == null || type.name().isBlank()) {
            errors.add(new CoreError("Type", "is mandatory."));
            return;
        }
        if (type != ProductType.PIZZA && type != ProductType.DRINK && type != ProductType.DESSERT) {
            errors.add(new CoreError("Type", "must be either 'PIZZA' or 'DRINK' or 'DESSERT'."));
        }
    }

}
