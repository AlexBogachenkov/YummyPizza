package yummypizza.core.validators.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.requests.order.DeleteOrderByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DeleteOrderByIdRequestValidatorTest {

    @Mock
    private OrderRepository repository;
    @InjectMocks
    private DeleteOrderByIdRequestValidator validator = new DeleteOrderByIdRequestValidator();

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        DeleteOrderByIdRequest request = new DeleteOrderByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        DeleteOrderByIdRequest request = new DeleteOrderByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        DeleteOrderByIdRequest request = new DeleteOrderByIdRequest(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        DeleteOrderByIdRequest request = new DeleteOrderByIdRequest(5L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        DeleteOrderByIdRequest request = new DeleteOrderByIdRequest(5L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

}