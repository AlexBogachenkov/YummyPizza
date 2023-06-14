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
import yummypizza.core.domain.*;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.FindOrderByIdResponse;
import yummypizza.core.validators.order.FindOrderByIdRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindOrderByIdServiceTest {

    @Mock
    private FindOrderByIdRequestValidator validator;
    @Mock
    private OrderRepository repository;
    @InjectMocks
    private FindOrderByIdService service;

    private FindOrderByIdRequest invalidRequest;
    private FindOrderByIdRequest validRequest;

    @BeforeAll
    public void setup() {
        invalidRequest = new FindOrderByIdRequest(-5L);
        validRequest = new FindOrderByIdRequest(4L);
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "is mandatory.")));
        FindOrderByIdResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Order ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        FindOrderByIdResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldSearchOrderInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).findById(4L);
    }

    @Test
    public void shouldReturnResponseWithFoundOrderWhenValidationPasses() {
        Order order = new Order(4L, new Cart(new User(), CartStatus.INACTIVE), OrderStatus.RECEIVED, new BigDecimal("12.40"),
                LocalDateTime.of(2023, 05, 25, 12, 19, 59),
                null, "Riga", "Brīvības iela", "134", "21A");
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(order));
        FindOrderByIdResponse response = service.execute(validRequest);
        assertNotNull(response.getFoundOrder());
        assertEquals(4L, response.getFoundOrder().get().getId());
        assertEquals("Brīvības iela", response.getFoundOrder().get().getStreet());
    }

}