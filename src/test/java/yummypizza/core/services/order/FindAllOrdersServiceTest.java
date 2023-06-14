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
import yummypizza.core.responses.order.FindAllOrdersResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindAllOrdersServiceTest {

    @Mock
    private OrderRepository repository;
    @InjectMocks
    private FindAllOrdersService service = new FindAllOrdersService();

    Order order1;
    Order order2;

    @BeforeAll
    public void setup() {
        order1 = new Order(new Cart(new User(), CartStatus.INACTIVE), OrderStatus.RECEIVED, new BigDecimal("12.40"),
                LocalDateTime.of(2023, 05, 25, 12, 19, 59),
                null, "Riga", "Brīvības iela", "134", "21A");
        order2 = new Order(new Cart(new User(), CartStatus.INACTIVE), OrderStatus.COMPLETED, new BigDecimal("110.00"),
                LocalDateTime.of(2023, 05, 25, 14, 45, 20),
                LocalDateTime.of(2023, 05, 25, 16, 12, 06),
                "Salaspils", "Skolas iela", "7", "23");
    }

    @Test
    public void shouldReturnFoundCarts() {
        Mockito.when(repository.findAll()).thenReturn(List.of(order1, order2));
        FindAllOrdersResponse response = service.execute();
        assertNotNull(response.getAllOrders());
        assertEquals(2, response.getAllOrders().size());
        assertEquals(order1, response.getAllOrders().get(0));
        assertEquals(order2, response.getAllOrders().get(1));
    }

}