package yummypizza.core.models;

import lombok.Data;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.Order;

import java.util.List;

@Data
public class OrderDto {

    private Order order;
    private List<CartProduct> orderProducts;

    public OrderDto(Order order, List<CartProduct> orderProducts) {
        this.order = order;
        this.orderProducts = orderProducts;
    }

}
