package pl.devbeard.librettino;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.order.application.port.PlaceOrderUseCase;
import pl.devbeard.librettino.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import pl.devbeard.librettino.order.application.port.QueryOrderUseCase;
import pl.devbeard.librettino.order.domain.OrderItem;
import pl.devbeard.librettino.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static pl.devbeard.librettino.catalog.application.port.CatalogUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private PlaceOrderUseCase placeOrderUseCase;
    private QueryOrderUseCase queryOrderUseCase;
    private final String title;
    private final Long limit;

    public ApplicationStartup(
            CatalogUseCase catalog,
            PlaceOrderUseCase placeOrderUseCase,
            QueryOrderUseCase queryOrderUseCase,
            @Value("${book.title}") String title,
            @Value("${book.limit}") Long limit) {
        this.catalog = catalog;
        this.placeOrderUseCase = placeOrderUseCase;
        this.queryOrderUseCase = queryOrderUseCase;
        this.title = title;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void placeOrder() {
        Book lotr1 = catalog.findOneByTitle("Lord of the Rings: The Fellowship of the Ring")
                            .orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book lotr2 = catalog.findOneByTitle("Lord of the Rings: The Two Towers")
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
                .item(new OrderItem(lotr1, 20))
                .item(new OrderItem(lotr2, 15))
                .build();

        PlaceOrderUseCase.PlaceOrderResponse response = placeOrderUseCase.placeOrder(command);
        System.out.println("Created ORDER with id: " + response.getOrderId());

        queryOrderUseCase.findAll()
                .forEach( order -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order);
                });
    }

    private void searchCatalog() {
        findByTitle();
        findAndUpdate();
        findByTitle();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Lord of the Rings: The Fellowship of the Ring", "J.R.R. Tolkien", 1952,new BigDecimal("59.90")));
        catalog.addBook(new CreateBookCommand("Lord of the Rings: The Two Towers", "J.R.R. Tolkien", 1954, new BigDecimal("63.90")));
        catalog.addBook(new CreateBookCommand("The Left Hand of God", "Paul Hoffman", 2010, new BigDecimal("49.90")));
        catalog.addBook(new CreateBookCommand("Angel of Storms", "Trudi Canavan", 2015, new BigDecimal("39.90")));
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Book updating....");
        catalog.findOneByTitleAndAuthor("Angel of Storms", "Trudi Canavan")
               .ifPresent(book -> {
                   UpdateBookCommand command = UpdateBookCommand
                           .builder()
                           .id(book.getId())
                           .title("Angel of Storms: Book Two of Millennium's Rule")
                           .build();
                   UpdateBookResponse response = catalog.updateBook(command);
                   System.out.println("Updating book result: " + response.isSuccess());
               });
    }
}