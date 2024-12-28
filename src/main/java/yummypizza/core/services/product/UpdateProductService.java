package yummypizza.core.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.product.UpdateProductResponse;
import yummypizza.core.services.ImageService;
import yummypizza.core.validators.product.UpdateProductRequestValidator;

import java.util.List;

@Service
public class UpdateProductService {

    @Autowired
    private ImageService imageService;
    @Autowired
    private UpdateProductRequestValidator validator;
    @Autowired
    private ProductRepository repository;

    public UpdateProductResponse execute(UpdateProductRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateProductResponse(errors);
        }

        String imageFileName = imageService.uploadImage(request.getImage());
        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(), request.getType(), imageFileName);
        product.setId(request.getId());
        return new UpdateProductResponse(repository.save(product));
    }

}
