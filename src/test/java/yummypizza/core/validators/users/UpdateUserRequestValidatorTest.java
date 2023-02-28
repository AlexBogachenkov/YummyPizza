package yummypizza.core.validators.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.user.UpdateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.validators.user.UpdateUserRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class UpdateUserRequestValidatorTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UpdateUserRequestValidator validator = new UpdateUserRequestValidator();

    private UpdateUserRequest request;

    @BeforeEach
    public void setup() {
        request = new UpdateUserRequest(5L, "Michael", "Smith",
                "m.smith@gmail.com", "password123", "25843748", UserRole.CLIENT);
        Mockito.when(repository.existsById(request.getId())).thenReturn(true);
    }

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        reset(repository);
        request.setId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        reset(repository);
        request.setId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        reset(repository);
        request.setId(-7L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        reset(repository);
        Mockito.when(repository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsNull() {
        request.setFirstName(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsEmpty() {
        request.setFirstName("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenFirstNameIsBlank() {
        request.setFirstName("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("First name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsNull() {
        request.setLastName(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsEmpty() {
        request.setLastName("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenLastNameIsBlank() {
        request.setLastName("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Last name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsNull() {
        request.setEmail(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsEmpty() {
        request.setEmail("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailIsBlank() {
        request.setEmail("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainLocalPart() {
        request.setEmail("@gmail.com");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainAtSymbol() {
        request.setEmail("m.smithgmail.com");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailNotContainDomain() {
        request.setEmail("m.smith@");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenEmailContainInvalidDomain() {
        request.setEmail("m.smith@gmailcom");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("has invalid format.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAnotherUserAlreadyHasSuchEmail() {
        request.setEmail("smithyym@gmail.com");
        Mockito.when(repository.findAllByEmailAndIdIsNot("smithyym@gmail.com", request.getId())).thenReturn(List.of(new User()));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Email", errors.get(0).getField());
        assertEquals("is already occupied by another user.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorWhenEmailWasNotChanged() {
        Mockito.when(repository.findAllByEmailAndIdIsNot(request.getEmail(), request.getId())).thenReturn(List.of());
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsNull() {
        request.setPassword(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsEmpty() {
        request.setPassword("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsBlank() {
        request.setPassword("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPasswordLengthIsLessThanEight() {
        request.setPassword("passwo1");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("must not be shorter than 8 characters.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsEight() {
        request.setPassword("password");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsBetweenEightAndTwenty() {
        request.setPassword("password12345");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldNotReturnErrorsWhenPasswordLengthIsTwenty() {
        request.setPassword("password12password12");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldReturnErrorWhenPasswordLengthIsMoreThanTwenty() {
        request.setPassword("password12password123");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Password", errors.get(0).getField());
        assertEquals("must not be longer than 20 characters.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsNull() {
        request.setPhone(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsEmpty() {
        request.setPhone("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneIsBlank() {
        request.setPhone("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneContainsNotDigits() {
        request.setPhone("2f232343!");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("must contain only digits.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPhoneNotContainsDigits() {
        request.setPhone("afa@dff!");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Phone", errors.get(0).getField());
        assertEquals("must contain only digits.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenPhoneContainsOnlyDigits() {
        request.setPhone("26458994");
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldReturnErrorWhenRoleIsNull() {
        request.setRole(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Role", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}