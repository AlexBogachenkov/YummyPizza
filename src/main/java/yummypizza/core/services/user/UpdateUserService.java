package yummypizza.core.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.UpdateUserRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.UpdateUserResponse;
import yummypizza.core.validators.user.UpdateUserRequestValidator;

import java.util.List;

@Service
public class UpdateUserService {

    @Autowired
    private UpdateUserRequestValidator validator;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UpdateUserResponse execute(UpdateUserRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateUserResponse(errors);
        }
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), request.getPhone(), request.getRole());
        user.setId(request.getId());
        return new UpdateUserResponse(repository.save(user));
    }

}
