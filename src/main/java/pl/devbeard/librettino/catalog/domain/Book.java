package pl.devbeard.librettino.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class Book {

    private Long id;
    private String title;
    private String author;
    private Integer year;
}
