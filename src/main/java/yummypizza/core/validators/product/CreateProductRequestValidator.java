package yummypizza.core.validators.product;

import org.springframework.stereotype.Component;
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
        return errors;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            errors.add(new CoreError("Nosaukums", "ir obligāts"));
            return;
        }
        if (name.length() > 50) {
            errors.add(new CoreError("Nosaukuma", "garumam ir jābūt robežās līdz 50 simboliem"));
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            errors.add(new CoreError("Apraksts", "ir obligāts"));
            return;
        }
        if (description.length() > 320) {
            errors.add(new CoreError("Apraksta", "garumam ir jābūt robežās līdz 320 simboliem"));
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            errors.add(new CoreError("Cena", "ir obligāta"));
            return;
        }
        if (price.compareTo(new BigDecimal(0)) <= 0) {
            errors.add(new CoreError("Cenai", "ir jābūt lielākai par 0"));
        }
    }

}
