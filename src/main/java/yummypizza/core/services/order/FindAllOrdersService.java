package yummypizza.core.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yummypizza.core.database.CartProductRepository;
import yummypizza.core.database.OrderRepository;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.Order;
import yummypizza.core.models.OrderDto;
import yummypizza.core.responses.order.FindAllOrdersResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindAllOrdersService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private CartProductRepository cartProductRepository;

    public FindAllOrdersResponse execute() {
        List<OrderDto> orderDtos = new ArrayList<>();

        List<Order> orders = repository.findAll();
        // Add products for each orders
        for (Order order : orders) {
            List<CartProduct> orderProducts = cartProductRepository.findByCartId(order.getCart().getId());
            orderDtos.add(new OrderDto(order, orderProducts));
        }

        return new FindAllOrdersResponse(orderDtos);
    }

}
