package yummypizza.core.requests.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import yummypizza.core.domain.ProductType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UpdateProductRequest {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductType type;

    public UpdateProductRequest(Long id, String name, String description, BigDecimal price, ProductType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
    }

}
