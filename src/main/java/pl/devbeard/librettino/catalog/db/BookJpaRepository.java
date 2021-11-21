package pl.devbeard.librettino.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.devbeard.librettino.catalog.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
