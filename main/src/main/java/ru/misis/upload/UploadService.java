package ru.misis.upload;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.misis.event.dto.EventDto;
import ru.misis.upload.dto.UploadedFileDto;
import ru.misis.user.dto.UserDto;

import java.util.UUID;

public interface UploadService {
    UserDto uploadUserAvatar(Authentication auth, UUID id, MultipartFile file);

    EventDto uploadEventPicture(Authentication auth, UUID id, MultipartFile file);

    UploadedFileDto uploadFile(Authentication auth, MultipartFile file);
}
