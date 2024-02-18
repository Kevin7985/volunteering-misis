package ru.misis.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.misis.auth.dto.Authorization;
import ru.misis.auth.exceptions.AuthFailed;
import ru.misis.auth.google.GoogleAuthService;
import ru.misis.auth.google.model.GoogleCredentials;
import ru.misis.auth.google.model.GoogleProfile;
import ru.misis.user.UserRepository;
import ru.misis.user.model.User;
import ru.misis.utils.StringGenerator;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Authorization authViaGoogle(String code) {
        GoogleCredentials auth = googleAuthService.getToken(code).orElseThrow(
                () -> new AuthFailed("Произошла ошибка при авторизации с помощью Google")
        );

        GoogleProfile profile = googleAuthService.getGoogleProfile(auth.getAccess_token()).orElseThrow(
                () -> new AuthFailed("Произошла ошибка при авторизации с помощью Google")
        );

        User user = null;
        Optional<User> foundByEmail = userRepository.findByEmail(profile.getEmail());
        if (foundByEmail.isEmpty()) {
            user = new User(
                    null,
                    null,
                    profile.getEmail(),
                    profile.getGiven_name(),
                    profile.getFamily_name(),
                    "",
                    "",
                    false,
                    false
            );

            user = userRepository.save(user);
        } else {
            user = foundByEmail.get();
        }

        Authorization authorization = new Authorization(
                user.getId(),
                StringGenerator.generateToken(),
                86400
        );

        redisTemplate.opsForValue().set(authorization.getToken(), user.getId().toString(), Duration.ofHours(24));
        return authorization;
    }
}