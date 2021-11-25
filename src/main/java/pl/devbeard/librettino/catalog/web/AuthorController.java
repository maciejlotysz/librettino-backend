package pl.devbeard.librettino.catalog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.devbeard.librettino.catalog.application.port.AuthorUseCase;
import pl.devbeard.librettino.catalog.domain.Author;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorUseCase authors;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authors.findAll();
    }
}
