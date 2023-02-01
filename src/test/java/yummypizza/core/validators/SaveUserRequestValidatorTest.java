package yummypizza.core.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveUserRequestValidatorTest {

    private SaveUserRequestValidator validator = new SaveUserRequestValidator();
    private SaveUserRequest correctRequest;

    @BeforeEach
    public void setup() {
        correctRequest = new SaveUserRequest("Michael", "Smith",
                "m.smith@gmail.com", "password123", "25843748", UserRole.CLIENT);
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsNull() {
        SaveUserRequest request = correctRequest;
        request.setFirstName(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsEmpty() {
        SaveUserRequest request = correctRequest;
        request.setFirstName("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsBlank() {
        SaveUserRequest request = correctRequest;
        request.setFirstName("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsNull() {
        SaveUserRequest request = correctRequest;
        request.setLastName(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsEmpty() {
        SaveUserRequest request = correctRequest;
        request.setLastName("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsBlank() {
        SaveUserRequest request = correctRequest;
        request.setLastName("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsNull() {
        SaveUserRequest request = correctRequest;
        request.setEmail(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsEmpty() {
        SaveUserRequest request = correctRequest;
        request.setEmail("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsBlank() {
        SaveUserRequest request = correctRequest;
        request.setEmail("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainLocalPart() {
        SaveUserRequest request = correctRequest;
        request.setEmail("@gmail.com");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainAtSymbol() {
        SaveUserRequest request = correctRequest;
        request.setEmail("m.smithgmail.com");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainDomain() {
        SaveUserRequest request = correctRequest;
        request.setEmail("m.smith@");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailContainInvalidDomain() {
        SaveUserRequest request = correctRequest;
        request.setEmail("m.smith@gmailcom");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsNull() {
        SaveUserRequest request = correctRequest;
        request.setPassword(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsEmpty() {
        SaveUserRequest request = correctRequest;
        request.setPassword("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsBlank() {
        SaveUserRequest request = correctRequest;
        request.setPassword("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordLengthIsLessThanEight() {
        SaveUserRequest request = correctRequest;
        request.setPassword("passwo1");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("must not be shorter than 8 characters.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsEight() {
        SaveUserRequest request = correctRequest;
        request.setPassword("password");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsBetweenEightAndTwenty() {
        SaveUserRequest request = correctRequest;
        request.setPassword("password12345");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsTwenty() {
        SaveUserRequest request = correctRequest;
        request.setPassword("password12password12");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldReturnErrorWhenPasswordLengthIsMoreThanTwenty() {
        SaveUserRequest request = correctRequest;
        request.setPassword("password12password123");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("must not be longer than 20 characters.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsNull() {
        SaveUserRequest request = correctRequest;
        request.setPhone(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsEmpty() {
        SaveUserRequest request = correctRequest;
        request.setPhone("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsBlank() {
        SaveUserRequest request = correctRequest;
        request.setPhone("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneContainsNotDigits() {
        SaveUserRequest request = correctRequest;
        request.setPhone("2f232343!");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("must contain only digits.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneNotContainsDigits() {
        SaveUserRequest request = correctRequest;
        request.setPhone("afa@dff!");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("must contain only digits.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenPhoneContainsOnlyDigits() {
        SaveUserRequest request = correctRequest;
        request.setPhone("26458994");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldReturnErrorWhenRoleIsNull() {
        SaveUserRequest request = correctRequest;
        request.setRole(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Role", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

}