package pl.devbeard.librettino.upload.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.devbeard.librettino.upload.domain.Upload;

@Repository
public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
