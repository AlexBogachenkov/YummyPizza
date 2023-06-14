package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.Order;
import yummypizza.core.domain.User;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.requests.order.UpdateOrderRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.cart.UpdateCartResponse;
import yummypizza.core.responses.order.UpdateOrderResponse;
import yummypizza.core.validators.cart.UpdateCartRequestValidator;
import yummypizza.core.validators.order.UpdateOrderRequestValidator;

import java.util.List;

@Service
public class UpdateOrderService {

    @Autowired
    private UpdateOrderRequestValidator validator;
    @Autowired
    private OrderRepository orderRepository;

    public UpdateOrderResponse execute(UpdateOrderRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new UpdateOrderResponse(errors);
        }
        Cart cart = new Cart();
        cart.setId(request.getCartId());
        Order order = new Order(request.getId(), cart, request.getStatus(), request.getAmount(),
                request.getDateCreated(), request.getDateCompleted(), request.getCity(), request.getStreet(),
                request.getBuildingNumber(), request.getApartmentNumber());
        return new UpdateOrderResponse(orderRepository.save(order));
    }

}
