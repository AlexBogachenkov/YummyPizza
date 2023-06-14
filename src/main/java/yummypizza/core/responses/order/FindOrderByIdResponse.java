package yummypizza.core.responses.order;

import lombok.Getter;
import yummypizza.core.domain.Order;
import yummypizza.core.responses.CoreError;
import yummypizza.core.responses.CoreResponse;

import java.util.List;
import java.util.Optional;

@Getter
public class FindOrderByIdResponse extends CoreResponse {

    private Optional<Order> foundOrder;

    public FindOrderByIdResponse(List<CoreError> errors) {
        super(errors);
    }

    public FindOrderByIdResponse(Optional<Order> foundOrder) {
        this.foundOrder = foundOrder;
    }

}
