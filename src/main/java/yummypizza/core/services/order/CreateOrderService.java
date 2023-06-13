package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.CreateOrderResponse;
import yummypizza.core.validators.order.CreateOrderRequestValidator;

import java.util.List;

@Service
public class CreateOrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private CreateOrderRequestValidator validator;

    public CreateOrderResponse execute(CreateOrderRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new CreateOrderResponse(errors);
        }
        Cart cart = new Cart();
        cart.setId(request.getCartId());
        Order order = new Order(cart, request.getStatus(), request.getAmount(), request.getDateCreated(),
                request.getDateCompleted(), request.getCity(), request.getStreet(), request.getBuildingNumber(), request.getApartmentNumber());
        return new CreateOrderResponse(repository.save(order));
    }

}