package yummypizza.core.validators.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.OrderStatus;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.CoreError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class CreateOrderRequestValidatorTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @InjectMocks
    private CreateOrderRequestValidator validator;

    private CreateOrderRequest request;

    @BeforeEach
    public void setup() {
        request = new CreateOrderRequest(4L, OrderStatus.PREPARING, new BigDecimal("15.50"),
                LocalDateTime.of(2023, 05, 25, 12, 19, 59),
                null, false, "Riga", "Brīvības iela", "134", "21A");
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(true);
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
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        Mockito.when(cartRepository.existsById(request.getCartId())).thenReturn(false);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("doesn't exist.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenOrderWithSuchCartIdAlreadyExists() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        Mockito.when(orderRepository.existsByCartId(request.getCartId())).thenReturn(true);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Cart ID", errors.get(0).getField());
        assertEquals("is already in use in another order.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStatusIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setStatus(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Status", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setAmount(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsNegative() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setAmount(new BigDecimal("-4.00"));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("must be positive.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenAmountIsZero() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setAmount(new BigDecimal(0));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Amount", errors.get(0).getField());
        assertEquals("must be positive.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCreatedIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setDateCreated(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date created", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorsWhenDateCreatedIsInFuture() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setDateCreated(LocalDateTime.now().plusHours(1));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date created", errors.get(0).getField());
        assertEquals("can not be in the future.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCompletedIsInFuture() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setDateCompleted(LocalDateTime.now().plusHours(1));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date completed", errors.get(0).getField());
        assertEquals("can not be in the future.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenDateCompletedIsBeforeDateCreated() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setDateCompleted(LocalDateTime.of(2023, 05, 25, 11, 19, 59));
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Date completed", errors.get(0).getField());
        assertEquals("can not be before the date created.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setCity(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsEmpty() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setCity("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenCityIsBlank() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setCity("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("City", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setStreet(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsEmpty() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setStreet("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenStreetIsBlank() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setStreet("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Street", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsNull() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setBuildingNumber(null);
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsEmpty() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setBuildingNumber("");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnErrorWhenBuildingNumberIsBlank() {
        Mockito.when(cartProductRepository.findByCartId(4L)).thenReturn(List.of(new CartProduct()));
        request.setBuildingNumber("   ");
        List<CoreError> errors = validator.validate(request);
        assertEquals(1, errors.size());
        assertEquals("Building number", errors.get(0).getField());
        assertEquals("is mandatory.", errors.get(0).getMessage());
    }

}