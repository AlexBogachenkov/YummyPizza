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
import yummypizza.core.domain.CartProduct;
import yummypizza.core.requests.cart_product.FindCartProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.FindCartProductByIdResponse;
import yummypizza.core.validators.cart_product.FindCartProductByIdRequestValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindCartProductByIdServiceTest {

    @Mock
    private FindCartProductByIdRequestValidator validator;
    @Mock
    private CartProductRepository repository;
    @InjectMocks
    private FindCartProductByIdService service;

    private FindCartProductByIdRequest invalidRequest;
    private FindCartProductByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new FindCartProductByIdRequest(-5L);
        validRequest = new FindCartProductByIdRequest(4L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart product ID", "must be a positive number.")));
        FindCartProductByIdResponse response = service.execute(invalidRequest);
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
    public void shouldSearchCartProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).findById(4L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        FindCartProductByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithFoundCartProductWhenValidationPasses() {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(4L);
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(cartProduct));
        FindCartProductByIdResponse response = service.execute(validRequest);
        assertNotNull(response.getFoundCartProduct());
        assertEquals(4L, response.getFoundCartProduct().get().getId());
        assertEquals(cartProduct, response.getFoundCartProduct().get());
    }

}