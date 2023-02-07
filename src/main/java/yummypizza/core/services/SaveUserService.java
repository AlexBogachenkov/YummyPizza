package yummypizza.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.SaveUserResponse;
import yummypizza.core.validators.SaveUserRequestValidator;

import java.util.List;

@Service
public class SaveUserService {

    @Autowired
    private SaveUserRequestValidator validator;
    @Autowired
    private UserRepository repository;

    public SaveUserResponse execute(SaveUserRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new SaveUserResponse(errors);
        }
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPassword(), request.getPhone(), request.getRole());
        user.setId(request.getId());
        return new SaveUserResponse(repository.save(user));
    }

}
