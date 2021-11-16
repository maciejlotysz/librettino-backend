package pl.devbeard.librettino.order.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.devbeard.librettino.order.application.port.PlaceOrderUseCase;
import pl.devbeard.librettino.order.application.port.QueryOrderUseCase;
import pl.devbeard.librettino.order.domain.Order;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findAll() {
        return queryOrder.findAll();
    }






}
