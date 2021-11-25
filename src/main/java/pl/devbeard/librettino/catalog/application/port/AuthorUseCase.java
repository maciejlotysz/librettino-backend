package pl.devbeard.librettino.catalog.application.port;

import pl.devbeard.librettino.catalog.domain.Author;

import java.util.List;

public interface AuthorUseCase {

    List<Author> findAll();
}
