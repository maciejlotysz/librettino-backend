package pl.devbeard.librettino.upload.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.devbeard.librettino.upload.application.port.UploadUseCase;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadUseCase upload;

    @GetMapping("/{id}")
    public ResponseEntity<UploadResponse> getUpload(@PathVariable String id) {
        return upload.getById(id)
                .map(file -> {
                    UploadResponse response = new UploadResponse(
                            file.getId(),
                            file.getContentType(),
                            file.getFilename(),
                            file.getCreatedAt()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> getUploadFile(@PathVariable String id) {
        return upload.getById(id)
                     .map(file -> {
                         String contentDisposition = "attachtments; filename=\"" + file.getFilename() + "\"";
                         Resource resource = new ByteArrayResource(file.getFile());
                         return ResponseEntity
                                 .ok()
                                 .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                                 .contentType(MediaType.parseMediaType(file.getContentType()))
                                 .body(resource);
                     })
                .orElse(ResponseEntity.notFound().build());
    }

    @Value
    @AllArgsConstructor
    static class UploadResponse {
        String id;
        String contentType;
        String filename;
        LocalDateTime createdAt;
    }
}
