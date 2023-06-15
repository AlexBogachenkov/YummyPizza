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
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.cart.FindCartByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.FindCartByIdResponse;
import yummypizza.core.validators.cart.FindCartByIdRequestValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCartByIdServiceTest {

    @Mock
    private FindCartByIdRequestValidator validator;
    @Mock
    private CartRepository repository;
    @InjectMocks
    private FindCartByIdService service;

    private FindCartByIdRequest invalidRequest;
    private FindCartByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new FindCartByIdRequest(-5L);
        validRequest = new FindCartByIdRequest(4L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "must be a positive number.")));
        FindCartByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Cart ID", response.getErrors().get(0).getField());
        assertEquals("must be a positive number.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "must be a positive number.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldSearchCartInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).findById(4L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        FindCartByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithFoundCartWhenValidationPasses() {
        User user = new User("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        Cart cart = new Cart(4L, user, CartStatus.ACTIVE);
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(cart));
        FindCartByIdResponse response = service.execute(validRequest);
        assertNotNull(response.getFoundCart());
        assertEquals(4L, response.getFoundCart().get().getId());
        assertEquals("Michael", response.getFoundCart().get().getUser().getFirstName());
    }

}