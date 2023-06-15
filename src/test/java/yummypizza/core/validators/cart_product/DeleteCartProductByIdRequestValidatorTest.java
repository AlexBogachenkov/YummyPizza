package yummypizza.core.validators.cart_product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DeleteCartProductByIdRequestValidatorTest {

    @Mock
    private CartProductRepository repository;
    @InjectMocks
    private DeleteCartProductByIdRequestValidator validator;

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        DeleteCartProductByIdRequest request = new DeleteCartProductByIdRequest(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        DeleteCartProductByIdRequest request = new DeleteCartProductByIdRequest(-3L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        DeleteCartProductByIdRequest request = new DeleteCartProductByIdRequest(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        DeleteCartProductByIdRequest request = new DeleteCartProductByIdRequest(4L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        DeleteCartProductByIdRequest request = new DeleteCartProductByIdRequest(4L);
        Mockito.when(repository.existsById(request.getId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

}