package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.CreateOrderResponse;
import yummypizza.core.validators.order.CreateOrderRequestValidator;

import java.util.List;

@Service
public class CreateOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CreateOrderRequestValidator validator;

    public CreateOrderResponse execute(CreateOrderRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new CreateOrderResponse(errors);
        }

        Cart cart = cartRepository.findById(request.getCartId()).get();
        cart.setStatus(CartStatus.INACTIVE);
        cartRepository.save(cart);
        cartRepository.save(new Cart(cart.getUser(), CartStatus.ACTIVE));

        Order order = new Order(cart, request.getStatus(), request.getAmount(), request.getDateCreated(),
                request.getDateCompleted(), request.isForTakeaway(), request.getCity(), request.getStreet(), request.getBuildingNumber(), request.getApartmentNumber());
        if (request.isForTakeaway()) {
            order.setCity(null);
            order.setStreet(null);
            order.setBuildingNumber(null);
            order.setApartmentNumber(null);
        }
        return new CreateOrderResponse(orderRepository.save(order));
    }

}