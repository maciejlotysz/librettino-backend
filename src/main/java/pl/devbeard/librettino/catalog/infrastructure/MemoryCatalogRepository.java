package pl.devbeard.librettino.catalog.infrastructure;

import org.springframework.stereotype.Repository;
import pl.devbeard.librettino.catalog.domain.Book;
import pl.devbeard.librettino.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
class MemoryCatalogRepository implements CatalogRepository {

    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);

    public MemoryCatalogRepository() {

    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Book book) {
     Long nextId = nextId();
     book.setId(nextId);
     storage.put(nextId, book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}
