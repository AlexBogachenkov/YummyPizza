package yummypizza.core.validators.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.ProductType;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateProductRequestValidator {

    @Autowired
    private ProductRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(UpdateProductRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        validateName(request.getName());
        validateDescription(request.getDescription());
        validatePrice(request.getPrice());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Produkta ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Produkta ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("Produkts", "ar šādu ID netika atrasts"));
        }
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
