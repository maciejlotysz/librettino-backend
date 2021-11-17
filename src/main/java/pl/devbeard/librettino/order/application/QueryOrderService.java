package pl.devbeard.librettino.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.order.application.port.QueryOrderUseCase;
import pl.devbeard.librettino.order.domain.Order;
import pl.devbeard.librettino.order.domain.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class QueryOrderService implements QueryOrderUseCase {

    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }
}
