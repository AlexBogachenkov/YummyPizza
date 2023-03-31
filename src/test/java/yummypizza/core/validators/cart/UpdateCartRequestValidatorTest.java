package yummypizza.core.validators.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class UpdateCartRequestValidatorTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UpdateCartRequestValidator validator;

    private UpdateCartRequest request;

    @BeforeEach
    public void setup() {
        request = new UpdateCartRequest(5L, 14L, CartStatus.INACTIVE);
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(true);
    }

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        request.setId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        request.setId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        request.setId(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsNull() {
        reset(userRepository);
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(true);
        request.setUserId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsZero() {
        reset(userRepository);
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(true);
        request.setUserId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsNegative() {
        reset(userRepository);
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(true);
        request.setUserId(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdNotExists() {
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(false);
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStatusIsNull() {
        Mockito.when(cartRepository.existsById(request.getId())).thenReturn(true);
        request.setStatus(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Status", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

}