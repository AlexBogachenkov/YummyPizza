package yummypizza.core.validators.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.UserRepository;
import yummypizza.core.requests.user.UpdateUserRequest;
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
        validatePassword(request.getPassword());
        validatePhone(request.getPhone());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Lietotāja ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Lietotāja ID", "ir jābūt pozitīvam skaitlim"));
            return;
        }
        if (!repository.existsById(id)) {
            errors.add(new CoreError("Lietotājs", "ar šādu ID netika atrasts"));
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            errors.add(new CoreError("Vārds", "ir obligāts"));
            return;
        }
        if (firstName.length() > 50) {
            errors.add(new CoreError("Vārda", "garumam ir jābūt robežās līdz 50 simboliem"));
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            errors.add(new CoreError("Uzvārds", "ir obligāts"));
            return;
        }
        if (lastName.length() > 50) {
            errors.add(new CoreError("Uzvārda", "garumam ir jābūt robežās līdz 50 simboliem"));
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            errors.add(new CoreError("E-pasts", "ir obligāts"));
            return;
        }
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\" +
                "x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]" +
                "*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]" +
                "|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\" +
                "x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if (!email.matches(emailRegex)) {
            errors.add(new CoreError("E-pastam", "ir jābūt formātā example@work.com"));
        }
        if (email.length() > 320) {
            errors.add(new CoreError("E-pasta", "garumam ir jābūt robežās līdz 320 simboliem"));
        }
        if (repository.findByEmail(email).isPresent()) {
            errors.add(new CoreError("Ievadīto e-pastu", "jau izmanto kāds cits lietotājs"));
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            errors.add(new CoreError("Parole", "ir obligāta"));
            return;
        }
        if (password.length() < 8) {
            errors.add(new CoreError("Parolei", "ir jābūt vismaz 8 simbolu garai"));
        }
        if (password.length() > 20) {
            errors.add(new CoreError("Parolei", "ir jāsatur līdz 20 simboliem"));
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            errors.add(new CoreError("Telefona numurs", "ir obligāts"));
            return;
        }
        if (phone.length() > 15) {
            errors.add(new CoreError("Telefona numura", "garumam ir jābūt līdz 15 simboliem"));
        }
        if (!phone.matches("[0-9]+")) {
            errors.add(new CoreError("Telefona numura", "ievadei ir atļauti tikai ciparu simboli"));
        }
    }

}
