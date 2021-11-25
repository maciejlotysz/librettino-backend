package pl.devbeard.librettino.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long bookId;
    int quantity;

    public OrderItem(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
