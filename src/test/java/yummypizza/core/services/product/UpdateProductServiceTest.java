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
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.UpdateProductResponse;
import yummypizza.core.validators.product.UpdateProductRequestValidator;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateProductServiceTest {

    @Mock
    private UpdateProductRequestValidator validator;
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private UpdateProductService service;

    private UpdateProductRequest invalidRequest;
    private UpdateProductRequest validRequest;
    private Product product;

    @BeforeAll
    public void setup() {
        invalidRequest = new UpdateProductRequest(null, "Pepperoni", "Real jam",
                new BigDecimal("9.80"), ProductType.PIZZA);
        validRequest = new UpdateProductRequest(5L, "Pepperoni", "Real jam",
                new BigDecimal("9.80"), ProductType.PIZZA);
        product = new Product(validRequest.getId(), validRequest.getName(), validRequest.getDescription(),
                validRequest.getPrice(), validRequest.getType());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("ID", "is mandatory.")));
        UpdateProductResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldUpdateProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(product);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        UpdateProductResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithUpdatedProductWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(product)).thenReturn(product);
        UpdateProductResponse response = service.execute(validRequest);
        assertNotNull(response.getUpdatedProduct());
        assertEquals("Pepperoni", response.getUpdatedProduct().getName());
        assertEquals("Real jam", response.getUpdatedProduct().getDescription());
    }

}