package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.requests.cart.DeleteCartByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.DeleteCartByIdResponse;
import yummypizza.core.validators.cart.DeleteCartByIdRequestValidator;

import java.util.List;

@Service
public class DeleteCartByIdService {

    @Autowired
    private CartRepository repository;
    @Autowired
    private DeleteCartByIdRequestValidator validator;

    public DeleteCartByIdResponse execute(DeleteCartByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new DeleteCartByIdResponse(errors);
        }
        repository.deleteById(request.getId());
        return new DeleteCartByIdResponse();
    }

}
