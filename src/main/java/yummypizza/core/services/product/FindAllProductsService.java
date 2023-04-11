package yummypizza.core.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.responses.product.FindAllProductsResponse;

@Service
public class FindAllProductsService {

    @Autowired
    private ProductRepository repository;

    public FindAllProductsResponse execute() {
        return new FindAllProductsResponse(repository.findAll());
    }

}
