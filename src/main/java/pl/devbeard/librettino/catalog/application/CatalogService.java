package pl.devbeard.librettino.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.catalog.domain.CatalogRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {

    private final CatalogRepository catalogRepository;

    @Override
    public List<Book> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().startsWith(title))
                                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().startsWith(title))
                                .findFirst();
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().startsWith(title))
                                .filter(book -> book.getAuthor().startsWith(author))
                                .findFirst();
    }

    @Override
    public void addBook(CreateBookCommand command) {
        Book book = command.toBook();
        catalogRepository.save(book);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return catalogRepository.findById(command.getId())
                                .map(book -> {
                                    Book updateBook = command.updateFields(book);
                                    catalogRepository.save(updateBook);
                                    return UpdateBookResponse.SUCCESS;
                                })
                                .orElseGet(() -> new UpdateBookResponse(false, List.of("Book not found with id: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        catalogRepository.removeById(id);
    }


}
