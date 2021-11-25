package pl.devbeard.librettino.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("authors")
    private Set<Book> books;

    @CreatedDate
    private LocalDateTime createdAt;

    public Author(String firsName, String lastName) {
        this.firstName = firsName;
        this.lastName = lastName;
    }
}
