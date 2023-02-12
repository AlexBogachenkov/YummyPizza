package yummypizza.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.UpdateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.UpdateUserResponse;
import yummypizza.core.validators.UpdateUserRequestValidator;

import java.util.List;

@Service
public class UpdateUserService {

    @Autowired
    private UpdateUserRequestValidator validator;
    @Autowired
    private UserRepository repository;

    public UpdateUserResponse execute(UpdateUserRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateUserResponse(errors);
        }
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPassword(), request.getPhone(), request.getRole());
        user.setId(request.getId());
        return new UpdateUserResponse(repository.save(user));
    }

}
