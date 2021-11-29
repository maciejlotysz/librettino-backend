package pl.devbeard.librettino.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.devbeard.librettino.catalog.application.port.CatalogUseCase;
import pl.devbeard.librettino.catalog.db.AuthorJpaRepository;
import pl.devbeard.librettino.catalog.db.BookJpaRepository;
import pl.devbeard.librettino.catalog.domain.Author;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.upload.application.port.UploadUseCase;
import pl.devbeard.librettino.upload.application.port.UploadUseCase.SaveUploadCommand;
import pl.devbeard.librettino.upload.domain.Upload;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {

    private final BookJpaRepository repository;
    private final UploadUseCase upload;
    private final AuthorJpaRepository authorRepository;

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
        return repository.findAllByTitleIgnoreCase(title);
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findFirstByTitleIgnoreCase(title);
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return repository.findByTitleContainsIgnoreCaseAndAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(title, author, author);
    }

//    @Override
//    public Optional<Book> findOneByTitleAndAuthor(String title, String author) {
//        return repository.findFirstByTitleAndAuthors_firstNameIgnoreCaseOrAuthors_lastNameIgnoreCase(title, author, author);
//    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findByAuthor(author);
    }

    @Override
    @Transactional
    public Book addBook(CreateBookCommand command) {
        Book book = toBook(command);
        return repository.save(book);
    }

    private Book toBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getYear(), command.getPrice());
        Set<Author> authors = fetchAuthorsByIds(command.getAuthors());
        updateBooks(book, authors);
        return book;
    }

    private void updateBooks(Book book, Set<Author> authors) {
        book.removeAuthors();
        authors.forEach(book::addAuthor);
    }

    private Set<Author> fetchAuthorsByIds(Set<Long> authors) {
        return authors
                .stream()
                .map(authorId -> authorRepository
                        .findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find author with id: " + authorId)))
                .collect(Collectors.toSet());
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository.findById(command.getId())
                         .map(book -> {
                             Book updateBook = updateFields(command, book);
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
                      Upload savedUpload = upload.saveUpload(new SaveUploadCommand(command.getFilename(), command.getContentType(), command.getFile()));
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

    private Book updateFields(UpdateBookCommand command, Book book) {
        if (command.getTitle() != null) {
            book.setTitle(command.getTitle());
        }
        if (command.getAuthors() != null && command.getAuthors().size() > 0) {
            updateBooks(book, fetchAuthorsByIds(command.getAuthors()));
        }
        if (command.getYear() != null) {
            book.setYear(command.getYear());
        }
        if (command.getPrice() != null) {
            book.setPrice(command.getPrice());
        }
        return book;
    }
}
