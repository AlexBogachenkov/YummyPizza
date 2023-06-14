package yummypizza.core.services.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.requests.order.DeleteOrderByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.DeleteOrderByIdResponse;
import yummypizza.core.validators.order.DeleteOrderByIdRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteOrderByIdServiceTest {

    @Mock
    private OrderRepository repository;
    @Mock
    private DeleteOrderByIdRequestValidator validator;
    @InjectMocks
    private DeleteOrderByIdService service;

    private DeleteOrderByIdRequest invalidRequest;
    private DeleteOrderByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new DeleteOrderByIdRequest(-5L);
        validRequest = new DeleteOrderByIdRequest(2L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "must be a positive number.")));
        DeleteOrderByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Order ID", response.getErrors().get(0).getField());
        assertEquals("must be a positive number.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "must be a positive number.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldDeleteOrderFromRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).deleteById(2L);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        DeleteOrderByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

}