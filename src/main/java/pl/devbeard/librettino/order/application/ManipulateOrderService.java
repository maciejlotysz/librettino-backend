package pl.devbeard.librettino.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.order.application.port.ManipulateOrderUseCase;
import pl.devbeard.librettino.order.db.OrderJpaRepository;
import pl.devbeard.librettino.order.domain.Order;
import pl.devbeard.librettino.order.domain.OrderStatus;

@Service
@RequiredArgsConstructor
class ManipulateOrderService implements ManipulateOrderUseCase {

    private final OrderJpaRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order
                .builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        repository.findById(id)
                .ifPresent(order -> {
                    order.setStatus(status);
                    repository.save(order);
                });
    }
}
