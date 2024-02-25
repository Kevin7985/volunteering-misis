package ru.misis.upload;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.misis.user.dto.UserDto;

import java.util.UUID;

public interface UploadService {
    UserDto uploadUserAvatar(Authentication auth, UUID id, MultipartFile file);
}
