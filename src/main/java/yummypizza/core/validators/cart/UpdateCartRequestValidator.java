package yummypizza.core.validators.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateCartRequestValidator {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(UpdateCartRequest request) {
        errors = new ArrayList<>();
        validateId(request.getId());
        validateUserId(request.getUserId());
        validateStatus(request.getStatus(), request.getUserId());
        return errors;
    }

    private void validateId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Cart ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Cart ID", "must be a positive number."));
            return;
        }
        if (!cartRepository.existsById(id)) {
            errors.add(new CoreError("Cart ID", "doesn't exist."));
        }
    }

    private void validateUserId(Long id) {
        if (id == null) {
            errors.add(new CoreError("User ID", "is mandatory."));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("User ID", "must be a positive number."));
            return;
        }
        if (!userRepository.existsById(id)) {
            errors.add(new CoreError("User ID", "doesn't exist."));
        }
    }

    private void validateStatus(CartStatus status, Long userId) {
        if (status == null || status.name().isBlank()) {
            errors.add(new CoreError("Status", "is mandatory."));
            return;
        }
        if (status != CartStatus.ACTIVE && status != CartStatus.INACTIVE) {
            errors.add(new CoreError("Status", "must be either 'ACTIVE' or 'INACTIVE'."));
        }
        if (status == CartStatus.ACTIVE && cartRepository.findAllByUserIdAndStatus(userId, status).size() > 0) {
            errors.add(new CoreError("Status", "'ACTIVE' is already set for one of the user's carts."));
        }
    }

}
