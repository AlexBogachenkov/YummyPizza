package yummypizza.core.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.UpdateUserProfileInformationRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.user.UpdateUserProfileInformationResponse;
import yummypizza.core.validators.user.UpdateUserProfileInformationRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateUserProfileInformationService {

    @Autowired
    private UpdateUserProfileInformationRequestValidator validator;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UpdateUserProfileInformationResponse execute(UpdateUserProfileInformationRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateUserProfileInformationResponse(errors);
        }

        User updatedUser = null;
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            updatedUser = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                    passwordEncoder.encode(request.getPassword()), request.getPhone(), request.getRole());
        } else {
            Optional<User> foundUser = repository.findById(request.getId());
            if (foundUser.isPresent()) {
                updatedUser = new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                        foundUser.get().getPassword(), request.getPhone(), request.getRole());
            }
        }
        updatedUser.setId(request.getId());
        return new UpdateUserProfileInformationResponse(repository.save(updatedUser));
    }

}
