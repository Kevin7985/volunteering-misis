package ru.misis.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Override
    public UUID uploadFile(MultipartFile file) {

        UUID filename = UUID.randomUUID();
        File file1 = new File("fileServer/src/main/resources/files/" + filename);

        try {
            file1.createNewFile();
        } catch (Exception e) {

        }

        try (OutputStream os = new FileOutputStream(file1)) {
            os.write(file.getBytes());
        } catch (Exception e) {
        }

        return filename;
    }

    @Override
    public Resource getFile(UUID id) {
        Path path = Path.of("");
        ByteArrayResource resource = null;

        try {
            File file = new File("fileServer/src/main/resources/files/" + id);
            resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            System.out.println(e);
        }

        return resource;
    }
}
