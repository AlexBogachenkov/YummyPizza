package yummypizza.core.responses.order;

import lombok.Getter;
import yummypizza.core.domain.Order;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;

@Getter
public class UpdateOrderResponse extends CoreResponse {

    private Order updatedOrder;

    public UpdateOrderResponse(List<CoreError> errors) {
        super(errors);
    }

    public UpdateOrderResponse(Order updatedOrder) {
        this.updatedOrder = updatedOrder;
    }

}
