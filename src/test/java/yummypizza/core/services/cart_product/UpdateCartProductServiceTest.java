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
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.UpdateCartProductResponse;
import yummypizza.core.validators.cart_product.UpdateCartProductRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateCartProductServiceTest {

    @Mock
    private UpdateCartProductRequestValidator validator;
    @Mock
    private CartProductRepository cartProductRepository;
    @InjectMocks
    private UpdateCartProductService service;

    private UpdateCartProductRequest invalidRequest;
    private UpdateCartProductRequest validRequest;
    private CartProduct cartProduct;
    private Cart cart;
    private Product product;

    @BeforeAll
    public void setup() {
        invalidRequest = new UpdateCartProductRequest(null, 4L, 5L, 3);
        validRequest = new UpdateCartProductRequest(6L, 4L, 5L, 3);

        cart = new Cart();
        cart.setId(validRequest.getCartId());

        product = new Product();
        product.setId(validRequest.getProductId());

        cartProduct = new CartProduct();
        cartProduct.setId(validRequest.getId());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(validRequest.getQuantity());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart product ID", "is mandatory.")));
        UpdateCartProductResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Cart product ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart product ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(cartProductRepository);
    }

    @Test
    public void shouldUpdateCartProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(cartProductRepository).save(cartProduct);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        UpdateCartProductResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithUpdatedCartProductWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(cartProductRepository.save(cartProduct)).thenReturn(cartProduct);
        UpdateCartProductResponse response = service.execute(validRequest);
        assertNotNull(response.getUpdatedCartProduct());
        assertEquals(6L, response.getUpdatedCartProduct().getId());
        assertEquals(3, response.getUpdatedCartProduct().getQuantity());
    }

}