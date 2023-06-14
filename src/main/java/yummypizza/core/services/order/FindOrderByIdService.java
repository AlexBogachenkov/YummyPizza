package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.FindOrderByIdResponse;
import yummypizza.core.validators.order.FindOrderByIdRequestValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FindOrderByIdService {

    @Autowired
    private FindOrderByIdRequestValidator validator;
    @Autowired
    private OrderRepository repository;

    public FindOrderByIdResponse execute(FindOrderByIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindOrderByIdResponse(errors);
        }
        Optional<Order> foundOrder = repository.findById(request.getId());
        return new FindOrderByIdResponse(foundOrder);
    }

}
