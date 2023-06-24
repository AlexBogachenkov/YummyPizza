package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.order.FindOrdersByUserIdRequest;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.order.FindOrdersByUserIdResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.validators.order.FindOrdersByUserIdRequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindOrdersByUserIdService {

    @Autowired
    private FindOrdersByUserIdRequestValidator validator;
    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private OrderRepository orderRepository;

    public FindOrdersByUserIdResponse execute(FindOrdersByUserIdRequest request) {
        List<CoreError> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            return new FindOrdersByUserIdResponse(errors);
        }

        FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(request.getUserId(), CartStatus.INACTIVE);
        List<Cart> userInactiveCarts = findCartsByUserIdAndStatusService.execute(findCartsRequest).getCarts();
        List<Order> userOrders = new ArrayList<>();
        for (Cart cart : userInactiveCarts) {
            Optional<Order> optionalOfOrder = orderRepository.findByCartId(cart.getId());
            optionalOfOrder.ifPresent(userOrders::add);
        }

        FindOrdersByUserIdResponse response = new FindOrdersByUserIdResponse();
        response.setFoundOrders(userOrders);
        return response;
    }

}
