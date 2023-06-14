package yummypizza.core.validators.order;

import org.junit.jupiter.api.Test;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindOrderByIdRequestValidatorTest {

    FindOrderByIdRequestValidator validator = new FindOrderByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        FindOrderByIdRequest request = new FindOrderByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        FindOrderByIdRequest request = new FindOrderByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        FindOrderByIdRequest request = new FindOrderByIdRequest(-4L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        FindOrderByIdRequest request = new FindOrderByIdRequest(6L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}