package yummypizza.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.UserRepository;
import yummypizza.core.requests.DeleteUserByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.DeleteUserByIdResponse;
import yummypizza.core.validators.DeleteUserByIdRequestValidator;

import java.util.List;

@Service
public class DeleteUserByIdService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private DeleteUserByIdRequestValidator validator;

    public DeleteUserByIdResponse execute(DeleteUserByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new DeleteUserByIdResponse(errors);
        }
        repository.deleteById(request.getId());
        return new DeleteUserByIdResponse(true);
    }

}
