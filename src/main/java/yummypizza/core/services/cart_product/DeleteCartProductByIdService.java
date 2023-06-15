package yummypizza.core.services.cart_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart_product.DeleteCartProductByIdResponse;
import yummypizza.core.validators.cart_product.DeleteCartProductByIdRequestValidator;

import java.util.List;

@Service
public class DeleteCartProductByIdService {

    @Autowired
    private CartProductRepository repository;
    @Autowired
    private DeleteCartProductByIdRequestValidator validator;

    public DeleteCartProductByIdResponse execute(DeleteCartProductByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new DeleteCartProductByIdResponse(errors);
        }
        repository.deleteById(request.getId());
        return new DeleteCartProductByIdResponse();
    }

}
