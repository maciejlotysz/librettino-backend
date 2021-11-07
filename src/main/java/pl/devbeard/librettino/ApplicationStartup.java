package pl.devbeard.librettino;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.devbeard.librettino.catalog.domain.Book;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String title;
    private final Long limit;

    public ApplicationStartup(CatalogUseCase catalog, @Value("${book.title}") String title, @Value("${book.limit}") Long limit) {
        this.catalog = catalog;
        this.title = title;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        initData();
        findByTitle();
    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Lord of the Rings: The Fellowship of the Ring", "J.R.R. Tolkien", 1952));
        catalog.addBook(new CreateBookCommand( "Lord of the Rings: The Two Towers", "J.R.R. Tolkien", 1954));
        catalog.addBook(new CreateBookCommand( "The Left Hand of God", "Paul Hoffman", 2010));
        catalog.addBook(new CreateBookCommand( "Angel of StormsBook Two of Millennium's Rule", "Trudi Canavan", 2015));
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle(title);
        books.forEach(System.out::println);
    }
}
