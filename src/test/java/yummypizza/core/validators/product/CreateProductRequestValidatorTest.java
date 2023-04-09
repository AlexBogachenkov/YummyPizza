package yummypizza.core.validators.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yummypizza.core.domain.ProductType;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateProductRequestValidatorTest {

    private CreateProductRequestValidator validator = new CreateProductRequestValidator();
    private CreateProductRequest request;

    @BeforeEach
    public void setup() {
        request = new CreateProductRequest("Pepperoni", "Real jam", new BigDecimal("9.80"), ProductType.PIZZA);
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
        request.setName("   ");
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
        request.setDescription("   ");
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
    public void shouldReturnErrorWhenTypeIsNull() {
        request.setType(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Type", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

}