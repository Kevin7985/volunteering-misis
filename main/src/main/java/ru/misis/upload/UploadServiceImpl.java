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
import ru.misis.event.EventRepository;
import ru.misis.event.dto.EventDto;
import ru.misis.event.model.Event;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.upload.dto.UploadedFileDto;
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
    private final EventRepository eventRepository;

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

        String fileId = uploadFileService(file);
        fileId = FILESERVER_URL + "/file/" + fileId.substring(1, fileId.length() - 1);

        user.setAvatar(fileId);
        user = userRepository.save(user);

        return mapperService.toUserDto(user);
    }

    @Override
    public EventDto uploadEventPicture(Authentication auth, UUID id, MultipartFile file) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(id);

        if (!event.getCreator().getId().equals(user.getId())) {
            throw new Forbidden();
        }

        String fileId = uploadFileService(file);
        fileId = FILESERVER_URL + "/file/" + fileId.substring(1, fileId.length() - 1);

        event.setPicture(fileId);
        event = eventRepository.save(event);

        return mapperService.toEventDto(event);
    }

    @Override
    public UploadedFileDto uploadFile(Authentication auth, MultipartFile file) {
        String fileId = uploadFileService(file);
        fileId = FILESERVER_URL + "/file/" + fileId.substring(1, fileId.length() - 1);

        return new UploadedFileDto(fileId);
    }

    private String uploadFileService(MultipartFile file) {
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
