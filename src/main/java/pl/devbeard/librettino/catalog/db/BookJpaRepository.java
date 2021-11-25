package pl.devbeard.librettino.catalog.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.devbeard.librettino.catalog.domain.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByTitleIgnoreCase(String title);

    Optional<Book> findFirstByTitleIgnoreCase(String title);


    List<Book> findByAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(String firstName, String lastName);

    @Query(
            value = " SELECT b FROM Book b  JOIN b.authors a " +
                    " WHERE " +
                    " lower(a.firstName) LIKE lower(concat('%', :name, '%')) " +
                    " OR lower(a.lastName) LIKE lower(concat('%', :name, '%')) ", nativeQuery = true
    )
    List<Book> findByAuthor(@Param("name") String author);

//    Optional<Book> findFirstByTitleAndAuthors_firstNameIgnoreCaseOrAuthors_lastNameIgnoreCase(String title, String firstName, String lastName);

    List<Book> findByTitleContainsIgnoreCaseAndAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(
            String title, String firstName, String lastName);
}