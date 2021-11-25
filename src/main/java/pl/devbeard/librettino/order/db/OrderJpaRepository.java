package pl.devbeard.librettino.order.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.devbeard.librettino.order.domain.Order;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
