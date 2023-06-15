package yummypizza.core.services.cart_product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.DeleteCartProductByIdResponse;
import yummypizza.core.validators.cart_product.DeleteCartProductByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteCartProductByIdServiceTest {

    @Mock
    private CartProductRepository repository;
    @Mock
    private DeleteCartProductByIdRequestValidator validator;
    @InjectMocks
    private DeleteCartProductByIdService service;

    private DeleteCartProductByIdRequest invalidRequest;
    private DeleteCartProductByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new DeleteCartProductByIdRequest(-5L);
        validRequest = new DeleteCartProductByIdRequest(2L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart product ID", "must be a positive number.")));
        DeleteCartProductByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Cart product ID", response.getErrors().get(0).getField());
        assertEquals("must be a positive number.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart product ID", "must be a positive number.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldDeleteCartProductFromRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).deleteById(validRequest.getId());
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        DeleteCartProductByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

}