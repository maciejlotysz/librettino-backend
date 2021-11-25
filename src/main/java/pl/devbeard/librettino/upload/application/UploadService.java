package pl.devbeard.librettino.upload.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.devbeard.librettino.upload.application.port.UploadUseCase;
import pl.devbeard.librettino.upload.db.UploadJpaRepository;
import pl.devbeard.librettino.upload.domain.Upload;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadService implements UploadUseCase {

    private final UploadJpaRepository repository;

    @Override
    public Upload saveUpload(SaveUploadCommand command) {
        Upload upload = new Upload(
                command.getFile(),
                command.getContentType(),
                command.getFilename()
        );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFilename() + " with id: " + upload.getId());
        return upload;

    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long coverId) {
        repository.deleteById(coverId);
    }
}
