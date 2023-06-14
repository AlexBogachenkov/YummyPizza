package yummypizza.core.responses.order;

import lombok.Getter;
import yummypizza.core.domain.Order;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindAllOrdersResponse extends CoreResponse {

    private List<Order> allOrders;

    public FindAllOrdersResponse(List<Order> allOrders) {
        this.allOrders = allOrders;
    }

}
