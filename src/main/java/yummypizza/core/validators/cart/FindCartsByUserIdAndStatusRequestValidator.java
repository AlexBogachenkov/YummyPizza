package yummypizza.core.validators.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindCartsByUserIdAndStatusRequestValidator {

    @Autowired
    private UserRepository userRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(FindCartsByUserIdAndStatusRequest request) {
        errors = new ArrayList<>();
        validateUserId(request.getUserId());
        validateCartStatus(request.getCartStatus());
        return errors;
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            errors.add(new CoreError("User ID", "is mandatory."));
            return;
        }
        if (userId <= 0) {
            errors.add(new CoreError("User ID", "must be a positive number."));
        }
        if (!userRepository.existsById(userId)) {
            errors.add(new CoreError("User ID", "doesn't exist."));
        }
    }

    private void validateCartStatus(CartStatus status) {
        if (status == null) {
            errors.add(new CoreError("Cart status", "is mandatory."));
            return;
        }
        if (status != CartStatus.INACTIVE && status != CartStatus.ACTIVE) {
            errors.add(new CoreError("Cart status", "must be either 'INACTIVE' or 'ACTIVE'."));
        }
    }

}
