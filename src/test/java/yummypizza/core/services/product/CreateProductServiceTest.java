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
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.CreateProductResponse;
import yummypizza.core.validators.product.CreateProductRequestValidator;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateProductServiceTest {

    @Mock
    private CreateProductRequestValidator validator;
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private CreateProductService service;

    private CreateProductRequest invalidRequest;
    private CreateProductRequest validRequest;
    private Product product;

    @BeforeAll
    public void setup() {
        invalidRequest = new CreateProductRequest(null, "Real jam", new BigDecimal("9.80"), ProductType.PIZZA);
        validRequest = new CreateProductRequest("Pepperoni", "Real jam", new BigDecimal("9.80"), ProductType.PIZZA);
        product = new Product(validRequest.getName(), validRequest.getDescription(), validRequest.getPrice(), validRequest.getType());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Name", "is mandatory.")));
        CreateProductResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Name", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Name", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldCreateProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(product);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        CreateProductResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithCreatedProductWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(product)).thenReturn(product);
        CreateProductResponse response = service.execute(validRequest);
        assertNotNull(response.getCreatedProduct());
        assertEquals("Pepperoni", response.getCreatedProduct().getName());
        assertEquals("Real jam", response.getCreatedProduct().getDescription());
    }

}