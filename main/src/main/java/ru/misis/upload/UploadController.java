package ru.misis.upload;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.misis.user.dto.UserDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class UploadController {
    private final UploadService uploadService;

    @PostMapping(value = "/users/{id}/update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Tag(name = "Users")
    public UserDto updateAvatar(
            Authentication auth,
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file) {
        return uploadService.uploadUserAvatar(auth, id, file);
    }
}
