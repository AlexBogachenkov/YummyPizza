package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.cart.DeleteCartByIdRequest;
import yummypizza.core.requests.order.DeleteOrderByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.DeleteCartByIdResponse;
import yummypizza.core.responses.order.DeleteOrderByIdResponse;
import yummypizza.core.validators.cart.DeleteCartByIdRequestValidator;
import yummypizza.core.validators.order.DeleteOrderByIdRequestValidator;

import java.util.List;

@Service
public class DeleteOrderByIdService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private DeleteOrderByIdRequestValidator validator;

    public DeleteOrderByIdResponse execute(DeleteOrderByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new DeleteOrderByIdResponse(errors);
        }
        repository.deleteById(request.getId());
        return new DeleteOrderByIdResponse();
    }

}
