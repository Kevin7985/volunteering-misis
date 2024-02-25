package ru.misis.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file);

    Resource getFile(UUID id);
}
