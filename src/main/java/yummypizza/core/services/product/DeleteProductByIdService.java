package yummypizza.core.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.DeleteProductByIdResponse;
import yummypizza.core.validators.product.DeleteProductByIdRequestValidator;

import java.util.List;

@Service
public class DeleteProductByIdService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private DeleteProductByIdRequestValidator validator;

    public DeleteProductByIdResponse execute(DeleteProductByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new DeleteProductByIdResponse(errors);
        }
        repository.deleteById(request.getId());
        return new DeleteProductByIdResponse();
    }

}
