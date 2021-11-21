package pl.devbeard.librettino.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import pl.devbeard.librettino.commons.Either;
import pl.devbeard.librettino.order.domain.OrderItem;
import pl.devbeard.librettino.order.domain.OrderStatus;
import pl.devbeard.librettino.order.domain.Recipient;

import java.util.List;

public interface ManipulateOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void deleteById(Long id);

    void updateOrderStatus(Long id, OrderStatus status);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;
    }

    class PlaceOrderResponse extends Either<String, Long> {
        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }
}
