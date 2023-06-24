package yummypizza.core.validators.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.UserRepository;
import yummypizza.core.requests.order.FindOrdersByUserIdRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class FindOrdersByUserIdRequestValidator {

    @Autowired
    private UserRepository userRepository;

    private List<CoreError> errors;

    public List<CoreError> validate(FindOrdersByUserIdRequest request) {
        errors = new ArrayList<>();
        validateUserId(request.getUserId());
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

}
