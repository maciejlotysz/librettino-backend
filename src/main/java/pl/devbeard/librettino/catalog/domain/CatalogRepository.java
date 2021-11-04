package pl.devbeard.librettino.catalog.domain;

import java.util.List;

public interface CatalogRepository {

    List<Book> findAll(String title);
}
