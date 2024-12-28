package yummypizza.core.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.CreateProductResponse;
import yummypizza.core.services.ImageService;
import yummypizza.core.validators.product.CreateProductRequestValidator;

import java.util.List;

@Service
public class CreateProductService {

    @Autowired
    private ImageService imageService;
    @Autowired
    private CreateProductRequestValidator validator;
    @Autowired
    private ProductRepository repository;

    public CreateProductResponse execute(CreateProductRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new CreateProductResponse(errors);
        }

        String imageFileName = imageService.uploadImage(request.getImage());
        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(), request.getType(), imageFileName);
        return new CreateProductResponse(repository.save(product));
    }

}
