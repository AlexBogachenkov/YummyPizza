package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.validators.cart.FindCartsByUserIdAndStatusRequestValidator;

import java.util.List;

@Service
public class FindCartsByUserIdAndStatusService {

    @Autowired
    private FindCartsByUserIdAndStatusRequestValidator validator;
    @Autowired
    private CartRepository repository;

    public FindCartsByUserIdAndStatusResponse execute(FindCartsByUserIdAndStatusRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            FindCartsByUserIdAndStatusResponse response = new FindCartsByUserIdAndStatusResponse();
            response.setErrors(errors);
            return response;
        }
        List<Cart> carts = repository.findAllByUserIdAndStatus(request.getUserId(), request.getCartStatus());
        return new FindCartsByUserIdAndStatusResponse(carts);
    }

}
