package pl.devbeard.librettino.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.devbeard.librettino.catalog.domain.Author;

@Repository
public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}
