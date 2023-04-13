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
import yummypizza.core.domain.Product;
import yummypizza.core.domain.ProductType;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.FindProductByIdResponse;
import yummypizza.core.validators.product.FindProductByIdRequestValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindProductByIdServiceTest {

    @Mock
    private FindProductByIdRequestValidator validator;
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private FindProductByIdService service;

    private FindProductByIdRequest invalidRequest;
    private FindProductByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new FindProductByIdRequest(-5L);
        validRequest = new FindProductByIdRequest(4L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Product ID", "is mandatory.")));
        FindProductByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Product ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Product ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldSearchProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).findById(4L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        FindProductByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithFoundProductWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.findById(validRequest.getId())).thenReturn(Optional.of(
                new Product("Pepperoni", "Real jam", new BigDecimal("9.80"), ProductType.PIZZA)));
        FindProductByIdResponse response = service.execute(validRequest);
        assertNotNull(response.getFoundProduct());
        assertEquals("Pepperoni", response.getFoundProduct().get().getName());
        assertEquals("Real jam", response.getFoundProduct().get().getDescription());
    }

}