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
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.responses.CoreError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreateCartRequestValidatorTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CreateCartRequestValidator validator;

    private CreateCartRequest request;

    @BeforeEach
    public void setup() {
        request = new CreateCartRequest(34L, CartStatus.ACTIVE);
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsNull() {
        request.setUserId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsZero() {
        request.setUserId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdIsNegative() {
        request.setUserId(-5L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenUserIdNotExists() {
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("User ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStatusIsNull() {
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(true);
        request.setStatus(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Status", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStatusActiveIsAlreadySetForOneOfUserCarts() {
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(true);
        Mockito.when(cartRepository.findAllByUserIdAndStatus(request.getUserId(), request.getStatus())).
                thenReturn(List.of(new Cart()));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Status", errors.get(0).getField());
        assertEquals("'ACTIVE' is already set for one of the user's carts.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        Mockito.when(userRepository.existsById(request.getUserId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}