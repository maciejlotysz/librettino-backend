package pl.devbeard.librettino.order.domain;

import lombok.Value;
import pl.devbeard.librettino.catalog.domain.Book;

@Value
public class OrderItem {

    Long bookId;
    int quantity;
}
