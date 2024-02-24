package ru.misis.auth.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.misis.auth.exceptions.AuthFailed;
import ru.misis.auth.google.model.GoogleProfile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {
    private final RestTemplateBuilder restTemplateBuilder;

    public Optional<GoogleProfile> getGoogleProfile(String token) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpEntity<String> entity = new HttpEntity<>("body", null);

        GoogleProfile profile = null;

        try {
            ResponseEntity<GoogleProfile> resp = restTemplate.exchange("https://oauth2.googleapis.com/tokeninfo?id_token=" + token, HttpMethod.GET, entity, GoogleProfile.class);
            profile = resp.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении профиля Google: " + e.getMessage());
            throw new AuthFailed("Произошла ошибка при авторизации с помощью Google");
        }

        if (profile == null) {
            return Optional.empty();
        }

        return Optional.of(profile);
    }
}
