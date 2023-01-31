package yummypizza.core.validators;

import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

public class SaveUserRequestValidator {

    private List<CoreError> errors;

    public List<CoreError> validate(SaveUserRequest request) {
        errors = new ArrayList<>();
        validateFirstName(request.getFirstName());
        validateLastName(request.getLastName());
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validatePhone(request.getPhone());
        validateRole(request.getRole());
        return errors;
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
        }
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            errors.add(new CoreError("Email", "has invalid format."));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            errors.add(new CoreError("Password", "is mandatory."));
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
        }
        if (!phone.matches("[0-9]+")) {
            errors.add(new CoreError("Phone", "must contain only numbers."));
        }
    }

    private void validateRole(UserRole role) {
        if (role == null) {
            errors.add(new CoreError("Role", "is mandatory."));
        }
    }

}
