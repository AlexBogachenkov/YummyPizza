package yummypizza.core.validators.cart;

import org.junit.jupiter.api.Test;
import yummypizza.core.requests.cart.FindCartByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindCartByIdRequestValidatorTest {

    FindCartByIdRequestValidator validator = new FindCartByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        FindCartByIdRequest request = new FindCartByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        FindCartByIdRequest request = new FindCartByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        FindCartByIdRequest request = new FindCartByIdRequest(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        FindCartByIdRequest request = new FindCartByIdRequest(8L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}