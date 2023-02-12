package yummypizza.core.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.UpdateUserRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateUserRequestValidator {

    @Autowired
    private UserRepository repository;

    private List<CoreError> errors;

    public List<CoreError> validate(UpdateUserRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        validateFirstName(request.getFirstName());
        validateLastName(request.getLastName());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validatePhone(request.getPhone());
        validateRole(request.getRole());
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

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            errors.add(new CoreError("First name", "is mandatory."));
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            errors.add(new CoreError("Last name", "is mandatory."));
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            errors.add(new CoreError("Email", "is mandatory."));
            return;
        }
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\" +
                "x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]" +
                "*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]" +
                "|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\" +
                "x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if (!email.matches(emailRegex)) {
            errors.add(new CoreError("Email", "has invalid format."));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            errors.add(new CoreError("Password", "is mandatory."));
            return;
        }
        if (password.length() < 8) {
            errors.add(new CoreError("Password", "must not be shorter than 8 characters."));
        }
        if (password.length() > 20) {
            errors.add(new CoreError("Password", "must not be longer than 20 characters."));
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            errors.add(new CoreError("Phone", "is mandatory."));
            return;
        }
        if (!phone.matches("[0-9]+")) {
            errors.add(new CoreError("Phone", "must contain only digits."));
        }
    }

    private void validateRole(UserRole role) {
        if (role == null || role.name().isBlank()) {
            errors.add(new CoreError("Role", "is mandatory."));
            return;
        }
        if (role != UserRole.CLIENT && role != UserRole.ADMIN) {
            errors.add(new CoreError("Role", "must be either 'CLIENT' or 'ADMIN'."));
        }
    }

}
