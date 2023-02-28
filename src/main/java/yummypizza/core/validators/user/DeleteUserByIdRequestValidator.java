package yummypizza.core.validators.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.UserRepository;
import yummypizza.core.requests.user.DeleteUserByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteUserByIdRequestValidator {

    @Autowired
    private UserRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(DeleteUserByIdRequest request) {
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
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("User ID", "doesn't exist."));
        }
    }

}
