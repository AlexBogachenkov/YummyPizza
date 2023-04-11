package yummypizza.core.services.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yummypizza.core.database.ProductRepository;
import yummypizza.core.domain.Product;
import yummypizza.core.domain.ProductType;
import yummypizza.core.responses.product.FindAllProductsResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindAllProductsServiceTest {

    @Mock
    private ProductRepository repository;
    @InjectMocks
    private FindAllProductsService service;

    private Product product1;
    private Product product2;

    @BeforeAll
    public void setup() {
        product1 = new Product("Pepperoni", "Real jam", new BigDecimal("9.80"), ProductType.PIZZA);
        product2 = new Product("Coca-Cola 2L", "Taste the feeling", new BigDecimal("3.00"), ProductType.DRINK);
    }

    @Test
    public void shouldReturnFoundProducts() {
        Mockito.when(repository.findAll()).thenReturn(List.of(product1, product2));
        FindAllProductsResponse response = service.execute();
        assertEquals(2, response.getAllProducts().size());
        assertEquals(product1, response.getAllProducts().get(0));
        assertEquals(product2, response.getAllProducts().get(1));
    }

}