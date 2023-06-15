package yummypizza.core.validators.cart_product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class UpdateCartProductRequestValidatorTest {

    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private UpdateCartProductRequestValidator validator;

    private UpdateCartProductRequest request;

    @BeforeEach
    public void setup() {
        request = new UpdateCartProductRequest(5L, 4L, 12L, 3);
        Mockito.when(cartProductRepository.existsById(request.getId())).thenReturn(true);
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(true);
        Mockito.when(productRepository.existsById(request.getProductId())).thenReturn(true);
    }

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        reset(cartProductRepository);
        request.setId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        reset(cartProductRepository);
        request.setId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        reset(cartProductRepository);
        request.setId(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        Mockito.when(cartProductRepository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart product ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsNull() {
        reset(cartRepository);
        request.setCartId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsNegative() {
        reset(cartRepository);
        request.setCartId(-4L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsZero() {
        reset(cartRepository);
        request.setCartId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdNotExists() {
        reset(cartRepository);
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenProductIdIsNull() {
        reset(productRepository);
        request.setProductId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenProductIdIsNegative() {
        reset(productRepository);
        request.setProductId(-4L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenProductIdIsZero() {
        reset(productRepository);
        request.setProductId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenProductIdNotExists() {
        reset(productRepository);
        Mockito.when(productRepository.existsById(request.getProductId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Product ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenQuantityIsNegative() {
        request.setQuantity(-7);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Quantity", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenQuantityIsZero() {
        request.setQuantity(0);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Quantity", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

}