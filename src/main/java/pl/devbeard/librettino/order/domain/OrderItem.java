package pl.devbeard.librettino.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.devbeard.librettino.jpa.BaseEntity;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem extends BaseEntity {

    private Long bookId;
    private int quantity;
}
