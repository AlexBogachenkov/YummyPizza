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
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.CreateCartResponse;
import yummypizza.core.validators.cart.CreateCartRequestValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateCartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CreateCartRequestValidator validator;
    @InjectMocks
    private CreateCartService service;

    private CreateCartRequest invalidRequest;
    private CreateCartRequest validRequest;
    private User user;
    private Cart cart;

    @BeforeAll
    public void setup() {
        invalidRequest = new CreateCartRequest(null, CartStatus.ACTIVE);
        validRequest = new CreateCartRequest(34L, CartStatus.ACTIVE);
        user = new User();
        user.setId(validRequest.getUserId());
        cart = new Cart(user, validRequest.getStatus());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        CreateCartResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("User ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeCartRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("User ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(cartRepository);
    }

    @Test
    public void shouldCreateCartInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(cartRepository).save(cart);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        CreateCartResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithCreatedCartWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        CreateCartResponse response = service.execute(validRequest);
        assertNotNull(response.getCartCreated());
        assertEquals(validRequest.getUserId(), response.getCartCreated().getUser().getId());
        assertEquals(validRequest.getStatus(), response.getCartCreated().getStatus());
    }

}