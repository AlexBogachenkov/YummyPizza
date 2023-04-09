package yummypizza.core.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yummypizza.core.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {



}
