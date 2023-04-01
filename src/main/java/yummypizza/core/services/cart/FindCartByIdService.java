package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.requests.cart.FindCartByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.FindCartByIdResponse;
import yummypizza.core.validators.cart.FindCartByIdRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FindCartByIdService {

    @Autowired
    private FindCartByIdRequestValidator validator;
    @Autowired
    private CartRepository repository;

    public FindCartByIdResponse execute(FindCartByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindCartByIdResponse(errors);
        }
        Optional<Cart> foundCart = repository.findById(request.getId());
        return new FindCartByIdResponse(foundCart);
    }

}
