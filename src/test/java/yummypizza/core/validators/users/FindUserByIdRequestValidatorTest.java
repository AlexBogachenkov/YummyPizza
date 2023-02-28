package yummypizza.core.validators.users;

import org.junit.jupiter.api.Test;
import yummypizza.core.requests.user.FindUserByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.validators.user.FindUserByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindUserByIdRequestValidatorTest {

    FindUserByIdRequestValidator validator = new FindUserByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        FindUserByIdRequest request = new FindUserByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        FindUserByIdRequest request = new FindUserByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        FindUserByIdRequest request = new FindUserByIdRequest(-2L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        FindUserByIdRequest request = new FindUserByIdRequest(8L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}