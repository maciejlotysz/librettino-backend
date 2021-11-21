package pl.devbeard.librettino.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.db.BookJpaRepository;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.upload.application.port.UploadUseCase;
import pl.devbeard.librettino.upload.application.port.UploadUseCase.SaveUploadCommand;
import pl.devbeard.librettino.upload.domain.Upload;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {

    private final BookJpaRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .findFirst();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .toList();
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return repository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findAll()
                                .stream()
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .toList();
    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = command.toBook();
        return repository.save(book);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                                .map(book -> {
                                    Book updateBook = command.updateFields(book);
                                    repository.save(updateBook);
                                    return UpdateBookResponse.SUCCESS;
                                })
                                .orElseGet(() -> new UpdateBookResponse(false, List.of("Book not found with id: " + command.getId())));
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.saveUpload(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }
}
