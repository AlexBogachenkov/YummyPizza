package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.User;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.UpdateCartResponse;
import yummypizza.core.validators.cart.UpdateCartRequestValidator;

import java.util.List;

@Service
public class UpdateCartService {

    @Autowired
    private UpdateCartRequestValidator validator;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    public UpdateCartResponse execute(UpdateCartRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateCartResponse(errors);
        }
        User user = userRepository.findById(request.getUserId()).get();
        Cart cart = new Cart(request.getId(), user, request.getStatus());
        return new UpdateCartResponse(cartRepository.save(cart));
    }

}
