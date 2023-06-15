package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.UpdateCartProductResponse;
import yummypizza.core.validators.cart_product.UpdateCartProductRequestValidator;

import java.util.List;

@Service
public class UpdateCartProductService {

    @Autowired
    private UpdateCartProductRequestValidator validator;
    @Autowired
    private CartProductRepository cartProductRepository;

    public UpdateCartProductResponse execute(UpdateCartProductRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateCartProductResponse(errors);
        }

        Cart cart = new Cart();
        cart.setId(request.getCartId());
        Product product = new Product();
        product.setId(request.getProductId());

        CartProduct cartProduct = new CartProduct(request.getId(), cart, product, request.getQuantity());
        return new UpdateCartProductResponse(cartProductRepository.save(cartProduct));
    }

}
