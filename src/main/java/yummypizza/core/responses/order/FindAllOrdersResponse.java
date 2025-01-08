package yummypizza.core.responses.order;

import lombok.Getter;
import yummypizza.core.models.OrderDto;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class FindAllOrdersResponse extends CoreResponse {

    private List<OrderDto> allOrders;

    public FindAllOrdersResponse(List<OrderDto> allOrders) {
        this.allOrders = allOrders;
    }

}
