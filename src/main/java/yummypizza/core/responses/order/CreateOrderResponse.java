package yummypizza.core.responses.order;

import lombok.Getter;
import yummypizza.core.domain.Order;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class CreateOrderResponse extends CoreResponse {

    private Order createdOrder;

    public CreateOrderResponse(List<CoreError> errors) {
        super(errors);
    }

    public CreateOrderResponse(Order createdOrder) {
        this.createdOrder = createdOrder;
    }

}
