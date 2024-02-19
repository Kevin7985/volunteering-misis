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
import ru.misis.auth.google.model.GoogleCredentials;
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

    @Value("${google.client.id}")
    private String CLIENT_ID;

    @Value("${google.client.secret}")
    private String CLIENT_SECRET;

    @Value("${google.redirect.uri}")
    private String REDIRECT_URI;

    public Optional<GoogleCredentials> getToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", URLDecoder.decode(code, StandardCharsets.UTF_8));
        params.put("redirect_uri", REDIRECT_URI);

        RestTemplate restTemplate = restTemplateBuilder.build();
        GoogleCredentials auth = null;

        try {
            auth = restTemplate.postForObject("https://www.googleapis.com/oauth2/v4/token?client_id={client_id}&client_secret={client_secret}&grant_type={grant_type}&code={code}&redirect_uri={redirect_uri}", null, GoogleCredentials.class, params);
        } catch (Exception e) {
            log.error("Ошибка при авторизации с помощью Google: " + e.getMessage());
            throw new AuthFailed("Произошла ошибка при авторизации с помощью Google");
        }

        if (auth == null) {
            return Optional.empty();
        }

        return Optional.of(auth);
    }

    public Optional<GoogleProfile> getGoogleProfile(String token) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        GoogleProfile profile = null;

        try {
            ResponseEntity<GoogleProfile> resp = restTemplate.exchange("https://www.googleapis.com/oauth2/v1/userinfo?alt=json", HttpMethod.GET, entity, GoogleProfile.class);
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
