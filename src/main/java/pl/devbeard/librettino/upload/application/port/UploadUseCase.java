package pl.devbeard.librettino.upload.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.devbeard.librettino.upload.domain.Upload;

import java.util.Optional;

public interface UploadUseCase {

    Upload saveUpload(SaveUploadCommand command);

    Optional<Upload> getById(String id);

    void removeById(String coverId);

    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;
    }


}
