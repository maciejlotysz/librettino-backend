package pl.devbeard.librettino.order.application.port;

import pl.devbeard.librettino.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<Order> findAll();

    Optional<Order> findById(Long id);
}
