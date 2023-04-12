package yummypizza.core.services.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.DeleteProductByIdResponse;
import yummypizza.core.validators.product.DeleteProductByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteProductByIdServiceTest {

    @Mock
    private ProductRepository repository;
    @Mock
    private DeleteProductByIdRequestValidator validator;
    @InjectMocks
    private DeleteProductByIdService service;

    private DeleteProductByIdRequest invalidRequest;
    private DeleteProductByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new DeleteProductByIdRequest(-5L);
        validRequest = new DeleteProductByIdRequest(2L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Product ID", "must be a positive number.")));
        DeleteProductByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Product ID", response.getErrors().get(0).getField());
        assertEquals("must be a positive number.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Product ID", "must be a positive number.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldDeleteProductFromRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).deleteById(2L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        DeleteProductByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

}