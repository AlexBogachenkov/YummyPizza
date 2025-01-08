package yummypizza.core.validators.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.responses.CoreError;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateCartRequestValidator {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    private List<CoreError> errors;

    public List<CoreError> validate(CreateCartRequest request) {
        errors = new ArrayList<>();
        validateUserId(request.getUserId());
        validateStatus(request.getStatus(), request.getUserId());
        return errors;
    }

    private void validateUserId(Long id) {
        if (id == null) {
            errors.add(new CoreError("Lietotāja ID", "ir obligāts"));
            return;
        }
        if (id <= 0) {
            errors.add(new CoreError("Lietotāja ID", "ir jābūt veselam pozitīvam skaitlim"));
            return;
        }
        if (!userRepository.existsById(id)) {
            errors.add(new CoreError("Lietotājs", "ar šādu ID netika atrasts"));
        }
    }

    private void validateStatus(CartStatus status, Long userId) {
        if (status == CartStatus.ACTIVE && cartRepository.findAllByUserIdAndStatus(userId, status).size() > 0) {
            errors.add(new CoreError("Grozs", "ar statusu 'Aktīvs' jau ir lietotājam ar ievadīto ID"));
        }
    }

}
