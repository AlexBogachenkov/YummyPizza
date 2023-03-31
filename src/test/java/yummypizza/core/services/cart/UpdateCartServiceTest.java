package yummypizza.core.services.cart;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.UpdateCartResponse;
import yummypizza.core.validators.cart.UpdateCartRequestValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateCartServiceTest {

    @Mock
    private UpdateCartRequestValidator validator;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UpdateCartService service;

    private UpdateCartRequest invalidRequest;
    private UpdateCartRequest validRequest;
    private User user;
    private Cart cart;

    @BeforeAll
    public void setup() {
        invalidRequest = new UpdateCartRequest(null, 12L, CartStatus.INACTIVE);
        validRequest = new UpdateCartRequest(4L, 12L, CartStatus.INACTIVE);
        user = new User(12L, "Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        cart = new Cart(4L, user, CartStatus.INACTIVE);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "is mandatory.")));
        UpdateCartResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Cart ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(cartRepository);
    }

    @Test
    public void shouldUpdateCartInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(userRepository.findById(validRequest.getUserId())).thenReturn(Optional.of(user));
        service.execute(validRequest);
        Mockito.verify(cartRepository).save(cart);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(userRepository.findById(validRequest.getUserId())).thenReturn(Optional.of(user));
        UpdateCartResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithUpdatedCartWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(userRepository.findById(validRequest.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        UpdateCartResponse response = service.execute(validRequest);
        assertNotNull(response.getUpdatedCart());
        assertEquals(4L, response.getUpdatedCart().getId());
        assertEquals(CartStatus.INACTIVE, response.getUpdatedCart().getStatus());
    }

}