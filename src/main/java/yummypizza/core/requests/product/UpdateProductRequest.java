package yummypizza.core.requests.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
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
    private MultipartFile image;

    public UpdateProductRequest(Long id, String name, String description, BigDecimal price, ProductType type, MultipartFile image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.image = image;
    }

}
