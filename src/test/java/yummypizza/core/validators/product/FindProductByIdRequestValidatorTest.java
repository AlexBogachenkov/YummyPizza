package yummypizza.core.validators.product;

import org.junit.jupiter.api.Test;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindProductByIdRequestValidatorTest {

    FindProductByIdRequestValidator validator = new FindProductByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        FindProductByIdRequest request = new FindProductByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        FindProductByIdRequest request = new FindProductByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        FindProductByIdRequest request = new FindProductByIdRequest(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        FindProductByIdRequest request = new FindProductByIdRequest(8L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}