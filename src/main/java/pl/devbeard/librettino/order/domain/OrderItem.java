package pl.devbeard.librettino.order.domain;

import lombok.Value;
import pl.devbeard.librettino.catalog.domain.Book;

@Value
public class OrderItem {

    Book book;
    int quantity;
}
