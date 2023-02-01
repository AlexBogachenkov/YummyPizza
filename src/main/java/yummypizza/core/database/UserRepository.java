package yummypizza.core.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yummypizza.core.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {



}
