package yummypizza.core.services.cart;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.User;
import yummypizza.core.domain.UserRole;
import yummypizza.core.responses.cart.FindAllCartsResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindAllCartsServiceTest {

    @Mock
    private CartRepository repository;
    @InjectMocks
    private FindAllCartsService service = new FindAllCartsService();

    private User user1;
    private User user2;
    private Cart cart1;
    private Cart cart2;

    @BeforeAll
    public void setup() {
        user1 = new User("Michael", "Smith", "m.smith@gmail.com",
                "password", "25436565", UserRole.CLIENT);
        user2 = new User("John", "Jordan", "j.jordan@gmail.com",
                "qwerty123", "22345344", UserRole.CLIENT);
        cart1 = new Cart(user1, CartStatus.ACTIVE);
        cart2 = new Cart(user2, CartStatus.ACTIVE);
    }

    @Test
    public void shouldReturnFoundCarts() {
        Mockito.when(repository.findAll()).thenReturn(List.of(cart1, cart2));
        FindAllCartsResponse response = service.execute();
        assertNotNull(response.getAllCarts());
        assertEquals(2, response.getAllCarts().size());
        assertEquals(cart1, response.getAllCarts().get(0));
        assertEquals(cart2, response.getAllCarts().get(1));
    }

}