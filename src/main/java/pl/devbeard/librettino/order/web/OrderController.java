package pl.devbeard.librettino.order.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.devbeard.librettino.order.application.port.ManipulateOrderUseCase;
import pl.devbeard.librettino.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.devbeard.librettino.order.application.port.QueryOrderUseCase;
import pl.devbeard.librettino.order.domain.OrderItem;
import pl.devbeard.librettino.order.domain.OrderStatus;
import pl.devbeard.librettino.order.domain.Recipient;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QueryOrderUseCase.RichOrder> findAll() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable Long id) {
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderCommand command) {
        return placeOrder
                .placeOrder(command.toPlaceOrderCommand())
                .handle(
                        orderId -> ResponseEntity.created(orderUri(orderId)).build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrderStatus(@PathVariable Long id, @RequestBody UpdateStatusCommand command) {
        OrderStatus orderStatus = OrderStatus
                .parseString(command.status)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Unknown status: " + command.status));
        placeOrder.updateOrderStatus(id, orderStatus);
    }
    @Data
    private static class CreateOrderCommand {
         List<OrderItemCommand> items;
         RecipientCommand recipient;

        PlaceOrderCommand toPlaceOrderCommand() {
            List<OrderItem> orderItems = items
                    .stream()
                    .map(item -> new OrderItem(item.bookId, item.quantity))
                    .collect(Collectors.toList());
            return new PlaceOrderCommand(orderItems, recipient.toRecipient());
        }
    }

    @Data
    static class OrderItemCommand {
        Long bookId;
        int quantity;
    }

    @Data
    static class RecipientCommand {
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;

        Recipient toRecipient() {
            return new Recipient(name, phone, street, city, zipCode, email);
        }
    }

    @Data
    static class UpdateStatusCommand {
        String status;
    }
}
