package yummypizza.core.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.FindUserByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.FindUserByIdResponse;
import yummypizza.core.validators.user.FindUserByIdRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FindUserByIdService {

    @Autowired
    private FindUserByIdRequestValidator validator;
    @Autowired
    private UserRepository repository;

    public FindUserByIdResponse execute(FindUserByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindUserByIdResponse(errors);
        }
        Optional<User> foundUser = repository.findById(request.getId());
        return new FindUserByIdResponse(foundUser);
    }

}
