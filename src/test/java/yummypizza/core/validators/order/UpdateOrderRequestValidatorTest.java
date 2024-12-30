package yummypizza.core.validators.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.*;
import yummypizza.core.requests.order.UpdateOrderRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class UpdateOrderRequestValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private UpdateOrderRequestValidator validator;

    private UpdateOrderRequest request;

    @BeforeEach
    public void setup() {
        request = new UpdateOrderRequest(2L, 4L, OrderStatus.PREPARING, new BigDecimal("15.50"),
                LocalDateTime.of(2023, 5, 25, 12, 19, 59),
                null, false, "Riga", "Brīvības iela", "134", "21A");
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(true);
        Mockito.when(orderRepository.existsById(request.getId())).thenReturn(true);
    }

    @Test
    public void shouldReturnErrorWhenIdIsNull() {
        reset(orderRepository);
        request.setId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsNegative() {
        reset(orderRepository);
        request.setId(-3L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenIdIsZero() {
        reset(orderRepository);
        request.setId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Order ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsNull() {
        reset(cartRepository);
        request.setCartId(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsNegative() {
        reset(cartRepository);
        request.setCartId(-3L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartIdIsZero() {
        reset(cartRepository);
        request.setCartId(0L);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("must be a positive number.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCartNotExist() {
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenOrderWithSuchCartIdAlreadyExists() {
        Cart cart = new Cart(4L, new User(), CartStatus.INACTIVE);
        Order order = new Order(cart, OrderStatus.PREPARING, new BigDecimal("15.50"),
                LocalDateTime.of(2023, 05, 25, 12, 19, 59),
                null, false, "Riga", "Brīvības iela", "134", "21A");
        Mockito.when(orderRepository.existsByCartId(request.getCartId())).thenReturn(true);
        Mockito.when(orderRepository.findByCartId(request.getCartId())).thenReturn(Optional.of(order));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is already in use in another order.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStatusIsNull() {
        request.setStatus(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Status", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsNull() {
        request.setAmount(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsNegative() {
        request.setAmount(new BigDecimal("-4.00"));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("must be positive.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsZero() {
        request.setAmount(new BigDecimal(0));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("must be positive.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCreatedIsNull() {
        request.setDateCreated(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date created", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorsWhenDateCreatedIsInFuture() {
        request.setDateCreated(LocalDateTime.now().plusHours(1));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date created", errors.get(0).getField());
        assertEquals("can not be in the future.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCompletedIsInFuture() {
        request.setDateCompleted(LocalDateTime.now().plusHours(1));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date completed", errors.get(0).getField());
        assertEquals("can not be in the future.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCompletedIsBeforeDateCreated() {
        request.setDateCompleted(LocalDateTime.of(2023, 05, 25, 11, 19, 59));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date completed", errors.get(0).getField());
        assertEquals("can not be before the date created.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsNull() {
        request.setCity(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsEmpty() {
        request.setCity("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsBlank() {
        request.setCity("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsNull() {
        request.setStreet(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsEmpty() {
        request.setStreet("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsBlank() {
        request.setStreet("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsNull() {
        request.setBuildingNumber(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsEmpty() {
        request.setBuildingNumber("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsBlank() {
        request.setBuildingNumber("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

}