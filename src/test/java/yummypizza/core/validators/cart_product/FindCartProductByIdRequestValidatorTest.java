package yummypizza.core.validators.cart_product;

import org.junit.jupiter.api.Test;
import yummypizza.core.requests.cart_product.FindCartProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindCartProductByIdRequestValidatorTest {

    FindCartProductByIdRequestValidator validator = new FindCartProductByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        FindCartProductByIdRequest request = new FindCartProductByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        FindCartProductByIdRequest request = new FindCartProductByIdRequest(-4L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        FindCartProductByIdRequest request = new FindCartProductByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        FindCartProductByIdRequest request = new FindCartProductByIdRequest(3L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}