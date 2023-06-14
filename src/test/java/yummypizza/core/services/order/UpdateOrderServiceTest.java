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
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.Order;
import yummypizza.core.domain.OrderStatus;
import yummypizza.core.requests.order.UpdateOrderRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.UpdateOrderResponse;
import yummypizza.core.validators.order.UpdateOrderRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateOrderServiceTest {

    @Mock
    private UpdateOrderRequestValidator validator;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private UpdateOrderService service;

    private UpdateOrderRequest invalidRequest;
    private UpdateOrderRequest validRequest;
    private Order order;
    private Cart cart;

    @BeforeAll
    public void setup() {
        invalidRequest = new UpdateOrderRequest(null, 4L, OrderStatus.PREPARING, new BigDecimal("15.50"),
                LocalDateTime.of(2023, 5, 25, 12, 19, 59),
                null, "Riga", "Brīvības iela", "134", "21A");
        validRequest = new UpdateOrderRequest(2L, 4L, OrderStatus.COMPLETED, new BigDecimal("25.34"),
                LocalDateTime.of(2023, 5, 25, 13, 52, 16),
                null, "Riga", "Skolas iela", "65", "33");
        cart = new Cart();
        cart.setId(validRequest.getCartId());
        order = new Order(validRequest.getId(), cart, validRequest.getStatus(), validRequest.getAmount(),
                validRequest.getDateCreated(), validRequest.getDateCompleted(), validRequest.getCity(),
                validRequest.getStreet(), validRequest.getBuildingNumber(), validRequest.getApartmentNumber());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "is mandatory.")));
        UpdateOrderResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Order ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Order ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(orderRepository);
    }

    @Test
    public void shouldUpdateOrderInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        UpdateOrderResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithUpdatedOrderWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        UpdateOrderResponse response = service.execute(validRequest);
        assertNotNull(response.getUpdatedOrder());
        assertEquals(2L, response.getUpdatedOrder().getId());
        assertEquals(OrderStatus.COMPLETED, response.getUpdatedOrder().getStatus());
    }

}