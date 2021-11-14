package pl.devbeard.librettino.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.catalog.domain.CatalogRepository;
import pl.devbeard.librettino.upload.application.port.UploadUseCase;
import pl.devbeard.librettino.upload.application.port.UploadUseCase.SaveUploadCommand;
import pl.devbeard.librettino.upload.domain.Upload;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {

    private final CatalogRepository catalogRepository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return catalogRepository.findById(id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .findFirst();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .toList();
    }

    @Override
    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return catalogRepository.findAll()
                                .stream()
                                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                                .toList();
    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = command.toBook();
        return catalogRepository.save(book);
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

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        catalogRepository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.saveUpload(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    book.setCoverId(savedUpload.getId());
                    catalogRepository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        catalogRepository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        catalogRepository.save(book);
                    }
                });
    }
}
