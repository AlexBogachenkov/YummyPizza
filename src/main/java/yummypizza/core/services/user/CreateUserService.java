package yummypizza.core.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.CreateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.CreateUserResponse;
import yummypizza.core.validators.user.CreateUserRequestValidator;

import java.util.List;

@Service
public class CreateUserService {

    @Autowired
    private CreateUserRequestValidator validator;
    @Autowired
    private UserRepository repository;

    public CreateUserResponse execute(CreateUserRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new CreateUserResponse(errors);
        }
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPassword(), request.getPhone(), request.getRole());
        return new CreateUserResponse(repository.save(user));
    }

}
