package yummypizza.core.services.cart_product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.Product;
import yummypizza.core.responses.cart_product.FindAllCartProductsResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindAllCartProductsServiceTest {

    @Mock
    private CartProductRepository repository;
    @InjectMocks
    private FindAllCartProductsService service;

    private CartProduct cartProduct1;
    private CartProduct cartProduct2;

    @BeforeAll
    public void setup() {
        cartProduct1 = new CartProduct(1L, new Cart(), new Product(), 4);
        cartProduct2 = new CartProduct(2L, new Cart(), new Product(), 3);
    }

    @Test
    public void shouldReturnFoundCarts() {
        Mockito.when(repository.findAll()).thenReturn(List.of(cartProduct1, cartProduct2));
        FindAllCartProductsResponse response = service.execute();
        assertNotNull(response.getAllCartProducts());
        assertEquals(2, response.getAllCartProducts().size());
        assertEquals(cartProduct1, response.getAllCartProducts().get(0));
        assertEquals(cartProduct2, response.getAllCartProducts().get(1));
    }

}