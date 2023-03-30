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
import yummypizza.core.requests.cart.DeleteCartByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.DeleteCartByIdResponse;
import yummypizza.core.validators.cart.DeleteCartByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteCartByIdServiceTest {

    @Mock
    private CartRepository repository;
    @Mock
    private DeleteCartByIdRequestValidator validator;
    @InjectMocks
    private DeleteCartByIdService service;

    private DeleteCartByIdRequest invalidRequest;
    private DeleteCartByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new DeleteCartByIdRequest(-5L);
        validRequest = new DeleteCartByIdRequest(2L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "must be a positive number.")));
        DeleteCartByIdResponse response = service.execute(invalidRequest);
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
    public void shouldDeleteCartFromRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).deleteById(2L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        DeleteCartByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

}