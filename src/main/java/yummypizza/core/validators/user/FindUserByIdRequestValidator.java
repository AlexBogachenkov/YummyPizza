package yummypizza.core.validators.user;

import org.springframework.stereotype.Component;
import yummypizza.core.requests.user.FindUserByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindUserByIdRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(FindUserByIdRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("User ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("User ID", "must be a positive number."));
        }
    }

}
