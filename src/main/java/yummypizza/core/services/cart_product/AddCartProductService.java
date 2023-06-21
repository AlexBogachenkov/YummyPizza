package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.AddCartProductResponse;
import yummypizza.core.validators.cart_product.AddCartProductRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class AddCartProductService {

    @Autowired
    private CartProductRepository repository;
    @Autowired
    private AddCartProductRequestValidator validator;

    public AddCartProductResponse execute(AddCartProductRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new AddCartProductResponse(errors);
        }

        CartProduct cartProduct;
        Optional<CartProduct> optionalOfCartProduct = repository.findByCartIdAndProductId(request.getCartId(), request.getProductId());
        if (optionalOfCartProduct.isPresent()) {
            cartProduct = optionalOfCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + request.getQuantity());
        } else {
            Cart cart = new Cart();
            cart.setId(request.getCartId());

            Product product = new Product();
            product.setId(request.getProductId());
            cartProduct = new CartProduct(cart, product, request.getQuantity());
        }
        return new AddCartProductResponse(repository.save(cartProduct));
    }

}
