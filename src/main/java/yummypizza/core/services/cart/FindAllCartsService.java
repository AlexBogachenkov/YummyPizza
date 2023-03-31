package yummypizza.core.services.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.responses.cart.FindAllCartsResponse;

@Service
public class FindAllCartsService {

    @Autowired
    private CartRepository repository;

    public FindAllCartsResponse execute() {
        return new FindAllCartsResponse(repository.findAll());
    }

}
