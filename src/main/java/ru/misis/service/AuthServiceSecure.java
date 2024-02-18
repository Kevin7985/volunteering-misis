package ru.misis.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.misis.user.UserRepository;
import ru.misis.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceSecure {
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Optional<Authentication> authenticate(HttpServletRequest req) {
        return extractToken(req).flatMap(this::checkToken);
    }

    private Optional<Authentication> checkToken(String token) {
        try {
            String userId = redisTemplate.opsForValue().get(token);
            if (userId != null) {
                Optional<User> user = userRepository.findById(UUID.fromString(userId));
                if (user.isEmpty()) {
                    return Optional.empty();
                }

                List<Role> roles = new ArrayList<>(List.of(Role.USER));

                if (user.get().getIsModerator()) {
                    roles.add(Role.MODERATOR);
                }

                if (user.get().getIsStaff()) {
                    roles.add(Role.STAFF);
                }

                Authentication authentication = createAuth(userId, roles);
                return Optional.of(authentication);
            }
        } catch (Exception e) {
            log.error("Ошибка во время проверки токена в Redis: " + e.getMessage());
            return Optional.empty();
        }

        return Optional.empty();
    }

    private Optional<String> extractToken(HttpServletRequest req) {
        try {
            String token = req.getHeader(HttpHeaders.AUTHORIZATION);
            if (token != null) {
                if (token.startsWith(BEARER_PREFIX)) {
                    token = token.substring(BEARER_PREFIX.length()).trim();
                    if (!token.isBlank()) {
                        return Optional.of(token);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ошибка во время парсинга токена: " + e.getMessage());
            return Optional.empty();
        }

        return Optional.empty();
    }

    private static Authentication createAuth(String user, List<Role> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(toList());
        return new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
    }

    private enum Role {
        USER,
        MODERATOR,
        STAFF
    }
}