package yummypizza.core.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yummypizza.core.domain.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {



}
