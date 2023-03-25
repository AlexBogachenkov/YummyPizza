package yummypizza.core.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yummypizza.core.domain.Cart;
import yummypizza.core.domain.CartStatus;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long > {

    List<Cart> findAllByUserIdAndStatus(Long userId, CartStatus status);

}
