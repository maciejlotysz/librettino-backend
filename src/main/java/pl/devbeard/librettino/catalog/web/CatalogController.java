package pl.devbeard.librettino.catalog.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.devbeard.librettino.catalog.domain.Book;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/catalog")
@RequiredArgsConstructor
@RestController
public class CatalogController {

    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAll(@RequestParam Optional<String> title,
                              @RequestParam Optional<String> author) {
        if (title.isPresent() && author.isPresent()) {
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        } else if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        } else if (author.isPresent()) {
            return catalog.findByAuthor(author.get());
        } else {
            return catalog.findAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBook(@RequestBody RestCreateBokCommand command) {
        Book book = catalog.addBook(command.toCommand());
        return ResponseEntity.created(createdBookUri(book)).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeById(id);
    }

    private URI createdBookUri(Book book) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + book.getId().toString()).build().toUri();
    }

    @Data
    private static class RestCreateBokCommand {
        private String title;
        private String author;
        private Integer year;
        private BigDecimal price;

        CreateBookCommand toCommand() {
            return new CreateBookCommand(title, author, year, price);
        }
    }
}
