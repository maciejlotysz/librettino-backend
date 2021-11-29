package pl.devbeard.librettino.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.devbeard.librettino.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Author extends BaseEntity {

    private String firstName;

    private String lastName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("authors")
    private Set<Book> books = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    public Author(String firsName, String lastName) {
        this.firstName = firsName;
        this.lastName = lastName;
    }

    public  void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }
    public  void removeBook(Book book) {
        books.remove(book);
        book.getAuthors().remove(this);
    }

}
