package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.User;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.CreateCartResponse;
import yummypizza.core.validators.cart.CreateCartRequestValidator;

import java.util.List;

@Service
public class CreateCartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreateCartRequestValidator validator;

    public CreateCartResponse execute(CreateCartRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new CreateCartResponse(errors);
        }
        User user = userRepository.findById(request.getUserId()).get();
        Cart cart = new Cart(user, request.getStatus());
        return new CreateCartResponse(cartRepository.save(cart));
    }

}
