package yummypizza.core.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yummypizza.core.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public boolean existsByCartId(Long id);

}
