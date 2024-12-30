package yummypizza.core.requests.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateOrderRequest {

    private Long cartId;
    private OrderStatus status;
    private BigDecimal amount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateCompleted;
    private boolean isForTakeaway;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;

    public CreateOrderRequest(Long cartId, OrderStatus status, BigDecimal amount, LocalDateTime dateCreated,
                              LocalDateTime dateCompleted, boolean isForTakeaway, String city, String street, String buildingNumber,
                              String apartmentNumber) {
        this.cartId = cartId;
        this.status = status;
        this.amount = amount;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.isForTakeaway = isForTakeaway;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public boolean isForTakeaway() {
        return this.isForTakeaway;
    }

    public boolean getIsForTakeaway() {
        return this.isForTakeaway;
    }

    public void setIsForTakeaway(boolean isForTakeaway) {
        this.isForTakeaway = isForTakeaway;
    }

}
