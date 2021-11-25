package pl.devbeard.librettino.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.catalog.application.port.AuthorUseCase;
import pl.devbeard.librettino.catalog.db.AuthorJpaRepository;
import pl.devbeard.librettino.catalog.domain.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService implements AuthorUseCase {

    private final AuthorJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }
}
