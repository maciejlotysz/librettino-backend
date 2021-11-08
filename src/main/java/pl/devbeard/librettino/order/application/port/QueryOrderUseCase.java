package pl.devbeard.librettino.order.application.port;

import pl.devbeard.librettino.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {

    List<Order> findAll();
}
