package pl.devbeard.librettino;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.devbeard.librettino.catalog.db.AuthorJpaRepository;
import pl.devbeard.librettino.catalog.domain.Author;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.order.application.port.ManipulateOrderUseCase;
import pl.devbeard.librettino.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.devbeard.librettino.order.application.port.QueryOrderUseCase;
import pl.devbeard.librettino.order.domain.OrderItem;
import pl.devbeard.librettino.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrderUseCase;
    private final QueryOrderUseCase queryOrderUseCase;
    private final AuthorJpaRepository authorRepository;

    @Override
    public void run(String... args) {
        initData();
        placeOrder();
    }

    private void placeOrder() {
        Book effectiveJava = catalog.findOneByTitle("effective Java")
                            .orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book javaPuzzlers = catalog.findOneByTitle("java Puzzlers")
                            .orElseThrow(() -> new IllegalStateException("Cannot find a book"));

        Recipient recipient = Recipient
                .builder()
                .name("Franciszek Dolas")
                .phone("123-456-789")
                .street("Al. Jerozolimskie 777")
                .city("Warszawa")
                .zipCode("00-007")
                .email("franek.dolas@example.pl")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(effectiveJava.getId(),20))
                .item(new OrderItem(javaPuzzlers.getId(), 15))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = placeOrderUseCase.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to create order: " + error
        );
        System.out.println(result);

        queryOrderUseCase.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    private void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorRepository.save(joshua);
        authorRepository.save(neal);

        CreateBookCommand effectiveJava = new CreateBookCommand(
                "Effective Java",
                Set.of(joshua.getId()),
                2005,
                new BigDecimal("79.99")
        );
        CreateBookCommand javaPuzzlers = new CreateBookCommand(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2018,
                new BigDecimal("99.99")
        );
        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);
    }


}