package yummypizza.core.responses.order;

import lombok.Getter;
import lombok.Setter;
import yummypizza.core.domain.Order;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
@Setter
public class FindOrdersByUserIdResponse extends CoreResponse {

    private List<Order> foundOrders;

    public FindOrdersByUserIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindOrdersByUserIdResponse() {

    }

}
