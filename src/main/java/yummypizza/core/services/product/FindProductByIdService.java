package yummypizza.core.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.FindProductByIdResponse;
import yummypizza.core.validators.product.FindProductByIdRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FindProductByIdService {

    @Autowired
    private FindProductByIdRequestValidator validator;
    @Autowired
    private ProductRepository repository;

    public FindProductByIdResponse execute(FindProductByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindProductByIdResponse(errors);
        }
        Optional<Product> foundProduct = repository.findById(request.getId());
        return new FindProductByIdResponse(foundProduct);
    }

}
