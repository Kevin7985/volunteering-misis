package ru.misis.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.user.UserRepository;
import ru.misis.user.dto.UserDto;
import ru.misis.user.model.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final UserRepository userRepository;

    @Value("${fileserver.url}")
    private String FILESERVER_URL;

    @Override
    public UserDto uploadUserAvatar(Authentication auth, UUID id, MultipartFile file) {
        User user = validationService.validateUser(id);

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            if (!user.getId().equals(UUID.fromString(auth.getName()))) {
                throw new Forbidden();
            }
        }

        String fileId = uploadFile(file);
        fileId = FILESERVER_URL + "/file/" + fileId.substring(1, fileId.length() - 1);

        user.setAvatar(fileId);
        user = userRepository.save(user);

        return mapperService.toUserDto(user);
    }

    private String uploadFile(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .postForEntity(FILESERVER_URL + "/upload-file", requestEntity, String.class);

        return response.getBody();
    }
}
