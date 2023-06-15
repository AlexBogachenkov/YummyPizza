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
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.AddCartProductResponse;
import yummypizza.core.validators.cart_product.AddCartProductRequestValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddCartProductServiceTest {

    @Mock
    private CartProductRepository repository;
    @Mock
    private AddCartProductRequestValidator validator;
    @InjectMocks
    private AddCartProductService service;

    private AddCartProductRequest invalidRequest;
    private AddCartProductRequest validRequest;
    private CartProduct cartProduct;
    private Cart cart;
    private Product product;

    @BeforeAll
    public void setup() {
        invalidRequest = new AddCartProductRequest(null, 4L, 3);
        validRequest = new AddCartProductRequest(6L, 4L, 3);

        cart = new Cart();
        cart.setId(validRequest.getCartId());

        product = new Product();
        product.setId(validRequest.getProductId());

        cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(validRequest.getQuantity());
    }

    @Test
    public void shouldReturnResponseWithErrorsWhenValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "is mandatory.")));
        AddCartProductResponse response = service.execute(invalidRequest);
        assertTrue(response.hasErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals("Cart ID", response.getErrors().get(0).getField());
        assertEquals("is mandatory.", response.getErrors().get(0).getMessage());
    }

    @Test
    public void shouldNotInvokeCartProductRepositoryWhenRequestValidationFails() {
        Mockito.when(validator.validate(invalidRequest)).thenReturn(List.of(new CoreError("Cart ID", "is mandatory.")));
        service.execute(invalidRequest);
        Mockito.verifyNoInteractions(repository);
    }

    @Test
    public void shouldAddCartProductInRepositoryWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        service.execute(validRequest);
        Mockito.verify(repository).save(cartProduct);
    }

    @Test
    public void shouldReturnResponseWithoutErrorsWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        AddCartProductResponse response = service.execute(validRequest);
        assertFalse(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithAddedCartProductWhenValidationPasses() {
        Mockito.when(validator.validate(validRequest)).thenReturn(List.of());
        Mockito.when(repository.save(cartProduct)).thenReturn(cartProduct);
        AddCartProductResponse response = service.execute(validRequest);
        assertNotNull(response.getCartProductAdded());
        assertEquals(validRequest.getCartId(), response.getCartProductAdded().getCart().getId());
        assertEquals(validRequest.getProductId(), response.getCartProductAdded().getProduct().getId());
    }

}