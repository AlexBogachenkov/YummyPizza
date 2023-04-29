package yummypizza.core.validators.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.ProductType;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class UpdateProductRequestValidatorTest {

    @Mock
    private ProductRepository repository;
    @InjectMocks
    private UpdateProductRequestValidator validator;

    private UpdateProductRequest request;

    @BeforeEach
    public void setup() {
        request = new UpdateProductRequest(5L, "Pepperoni", "Real jam", new BigDecimal("9.80"), ProductType.PIZZA);
        Mockito.when(repository.existsById(request.getId())).thenReturn(true);
    }

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        reset(repository);
        request.setId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        reset(repository);
        request.setId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        reset(repository);
        request.setId(-7L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdNotExists() {
        Mockito.when(repository.existsById(request.getId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenNameIsNull() {
        request.setName(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenNameIsEmpty() {
        request.setName("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenNameIsBlank() {
        request.setName("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Name", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDescriptionIsNull() {
        request.setDescription(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Description", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDescriptionIsEmpty() {
        request.setDescription("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Description", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDescriptionIsBlank() {
        request.setDescription("     ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Description", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPriceIsNull() {
        request.setPrice(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Price", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenPriceIsNegative() {
        request.setPrice(new BigDecimal(-4));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Price", errors.get(0).getField());
        assertEquals("must be positive.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenRoleIsNull() {
        request.setType(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Type", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldNotReturnErrorsWhenRequestIsCorrect() {
        List<CoreError> errors = validator.validate(request);
        assertEquals(0, errors.size());
    }

}